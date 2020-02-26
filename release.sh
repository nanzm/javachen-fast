#!/bin/bash

# https://github.com/fabric8io/docker-maven-plugin/blob/master/release.sh

# Exit if any error occurs
# Fail on a single failed command in a pipeline (if supported)
set -o pipefail

# Save global script args, use "build" as default
if [ -z "$1" ]; then
    ARGS=("")
else
    ARGS=("$@")
fi

# Fail on error and undefined vars (please don't use global vars, but evaluation of functions for return values)
set -eu

usage() {
    cat - <<EOT
Release ads
    --dry-run                 Dry run, which performs the whole build but does no tagging, artefact
                              upload or pushing Docker images
    --release-version <ver>   Version to release (e.g. "1.2.1"). One version arg is mandatory
    --release-comment <comment> Comment to release.
    --settings <file>         Path to a custom settings.xml to use for the release.
                              This file must contain all the credentials to be used for Sonatype.
                              By default ~/.m2/settings.xml is used.
    --local-maven-repo <dir>  Local dir for holding the local Maven repo cache. If not given, then a new
                              temporary directory will be used (and removed after the release)
    --no-git-push             Don't push the release tag (and symbolic major.minor tag) at the end
    --git-remote              Name of the git remote to push to. If not given, its trying to be pushed
                              to the git remote to which the currently checked out branch is attached to.
                              Works only when on a branch, not when checked out directly.
    --log <log-file>          Write full log to <log-file>, only print progress to screen
EOT
}


# Dir where this script is located
basedir() {
    # Default is current directory
    local script=${BASH_SOURCE[0]}

    # Resolve symbolic links
    if [ -L $script ]; then
        if readlink -f $script >/dev/null 2>&1; then
            script=$(readlink -f $script)
        elif readlink $script >/dev/null 2>&1; then
            script=$(readlink $script)
        elif realpath $script >/dev/null 2>&1; then
            script=$(realpath $script)
        else
            echo "ERROR: Cannot resolve symbolic link $script"
            exit 1
        fi
    fi

    local dir=$(dirname "$script")
    local full_dir=$(cd "${dir}" && pwd)
    echo ${full_dir}
}

# Checks if a flag is present in the arguments.
hasflag() {
    filters="$@"
    for var in "${ARGS[@]}"; do
        for filter in $filters; do
          if [ "$var" = "$filter" ]; then
              echo 'true'
              return
          fi
        done
    done
}

# Read the value of an option.
readopt() {
    filters="$@"
    next=false
    for var in "${ARGS[@]}"; do
        if $next; then
            echo $var
            break;
        fi
        for filter in $filters; do
            if [[ "$var" = ${filter}* ]]; then
                local value="${var//${filter}=/}"
                if [ "$value" != "$var" ]; then
                    echo $value
                    return
                fi
                next=true
            fi
        done
    done
}

check_error() {
    local msg="$*"
    if [ "${msg//ERROR/}" != "${msg}" ]; then
        echo "==============================================================="
        echo $msg
        exit 1
    fi
}

get_release_version() {
    local release_version=$(readopt --release-version)
    if [ -z "${release_version}" ]; then
        echo "ERROR: Please specify --release-version"
        return
    fi
    echo $release_version
}

get_release_comment() {
    local release_comment=$(readopt --release-comment)
    if [ -z "${release_comment}" ]; then
        release_comment= $(get_release_version)
    fi
    echo release_comment
}

get_pom_version() {
    # mvn -N help:evaluate -Dexpression="project.version"
    local pom_version=$(mvn -N help:evaluate -Dexpression="project.version" | grep  '^[0-9]' | sed -e 's/\([0-9]*\.[0-9]*\).*/\1/')
    if [ -z "${pom_version}" ]; then
        echo "ERROR: Cannot extract version from pom.xml"
        exit 1
    fi
}

check_git_clean() {
    echo "==== Checking for clean Git Repo"
    set +e
    git diff-index --quiet HEAD --
    local git_uncommitted=$?
    set -e
    if [ $git_uncommitted != 0 ]; then
       echo "Untracked or changed files exist. Please run release on a clean repo"
       git status
       exit 1
    fi
}

update_pom_versions() {
    local version="$1"
    local maven_opts="$2"

    echo "==== Updating pom.xml versions to $version"
    mvn ${maven_opts} versions:set -DnewVersion=$version -DprocessAllModules=true -DgenerateBackupPoms=false
}

extract_maven_opts() {
    maven_opts="$1"

    local settings_xml=$(readopt --settings-xml --settings)
    if [ -n "${settings_xml}" ]; then
        maven_opts="$maven_opts -s $settings_xml"
    fi

    echo $maven_opts
}

mvn_clean_install() {
    local maven_opts="$1"

    echo "==== Running 'mvn clean install -Dmaven.test.skip=true'"
    mvn ${maven_opts} clean install -Dmaven.test.skip=true
}

update_k8s_versions() {
    local old_version=$(cat k8s/test/deployment.yaml|grep harbor.test.wesine.com.cn|awk -F ":" '{print $3}')
    local new_version="$1"

    echo "==== Updating k8s image versions from $old_version to $new_version"
    sed -i '' "s/$old_version/$new_version/g" k8s/test/deployment.yaml
}

update_dockerfile_jar_versions() {
    local old_version=$(cat Dockerfile|grep "target/ads" |awk '{print $3}'|awk -F '-' '{print $2}' |awk -F '.jar' '{print $1}')
    local new_version="$1"

    echo "==== Updating Dockerfile jar versions from $old_version to $new_version"
    sed -i '' "s/$old_version/$new_version/g" Dockerfile
}

git_commit_files() {
    local version=$1

    echo "==== Committing files to local git"
    git add pom.xml
    git commit -am "Release version $version" >/dev/null 2>&1
}

git_tag_release() {
    local release_version=${1}

    echo "==== Tagging version $release_version"
    local release_comment= $(get_release_comment)

    git tag -f -a "$release_version" -m "$release_comment"
}

git_push() {
    local release_version=${1:-}

    if [ ! $(hasflag --no-git-push) ] && [ ! $(hasflag --dry-run -n) ]; then
        local remote=$(readopt --git-remote)
        if [ -z "${remote}" ]; then
            # Push to the remote attached to the local checkout branch
            remote=$(git for-each-ref --format='%(upstream:short)' $(git symbolic-ref -q HEAD) | sed -e 's/\([^\/]*\)\/.*/\1/')
            if [ -z "${remote}" ]; then
              echo "ERROR: Cannot find remote repository to git push to"
              exit 1
            fi
        fi

        echo "==== Pushing to Git"
        if [ -n "$release_version" ]; then
            echo "* Pushing $release_version"
            if [ $(hasflag --snapshot-release) ]; then
                # Force push to allow multiple releases per day
                git push -f -u $remote $release_version
            else
                git push -f -u $remote $release_version
            fi
        fi
    fi
}

# ===================================================================================

if [ $(hasflag --help -h) ]; then
   usage
   exit 0
fi

cd $(basedir)
release_version=$(get_release_version)
check_error "$release_version"

# Write to logfile if requested
if [ $(readopt --log) ]; then
    logfile=$(readopt --log)
    touch $logfile
    tail -f $logfile
    tail_pid=$!
    trap "kill $tail_pid" EXIT
    exec >>$logfile 2>&1
    sleep 1
fi

# Verify that there are no modified file in git repo
check_git_clean

# Temporary local repository to guarantee a clean build
tmp_maven_opts=""
if [ $(readopt --local-maven-repo) ]; then
    local_maven_repo=$(readopt --local-maven-repo)
    tmp_maven_opts="-Dmaven.repo.local=$local_maven_repo --batch-mode"
fi

# Calculate common maven options
maven_opts="$(extract_maven_opts "$tmp_maven_opts")"
check_error $maven_opts

# Set pom.xml version to the given release_version
update_pom_versions "$release_version" "$maven_opts"

update_k8s_versions "$release_version"

#update_dockerfile_jar_versions "$release_version"

# Make a clean install
mvn_clean_install "$maven_opts"

# For a test run, we are done
if [ $(hasflag --dry-run) ]; then
    echo "==== Dry run finished, nothing has been committed"
    echo "==== Use 'git reset --hard' to cleanup"
    git reset --hard
    exit 0
fi

# ========================================================================
# tag, release, push
# --------------------------

# Git Commit all changed files
git_commit_files "$release_version"

# Tag the release version
git_tag_release "$release_version"

# Push everything (if configured)
git_push "$release_version"
