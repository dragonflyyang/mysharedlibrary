def call(String name = 'human') {
  
    def _s = name
    echo "Hello, ${name}."
    sh 'npm install'
    GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true).trim()
    GIT_AUTHOR_NAME = sh (script: "git show -s --pretty=%an", returnStdout: true).trim()
    GIT_DATE = sh (script: "git show -s --pretty=%ai", returnStdout: true).trim()
    GIT_COMMIT_CONTENT = sh (script: "git show -s --pretty=%s", returnStdout: true).trim()
    GIT_DIFF_CONTENT = sh (script: "git diff --stat ${GIT_COMMIT_HASH}^1 ${GIT_COMMIT_HASH}", returnStdout: true).trim()
    JENKINS_JOB_NAME = URLDecoder.decode( "${JOB_NAME}", "UTF-8" );
    try {
           sh 'tslint "assets/**/*.ts" --exclude "assets/**/*.d.ts" --format junit --out jslint-test-results.xml'
    } catch(Exception e) {
         slackSend color: "danger", message: "*TSLint檢查有誤：* ${JENKINS_JOB_NAME},\n上傳人員: `${GIT_AUTHOR_NAME}`,\nDate: `${GIT_DATE}`,\nCommit: `${GIT_COMMIT_CONTENT}`,\nDiff:\n ```${GIT_DIFF_CONTENT}``` Hash: `${GIT_COMMIT_HASH}`"
         junit 'jslint-test-results.xml'
         assert false
    }
}