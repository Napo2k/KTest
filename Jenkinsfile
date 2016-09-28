def javaEnv() {
  def javaHome = tool 'Java 8'
  ["PATH=${env.PATH}:${javaHome}\\bin", "JAVA_HOME=${javaHome}"]
}

stage('test') {
    node() {
        git url: 'https://github.com/Napo2k/KTest.git'
        withEnv(javaEnv()) {
            bat 'gradle clean test'
        }
        junit 'build\\test-results\\test\\TEST-org.gradle.examples.web.ServletTest.xml'
    }
}

stage('archiving') {
    node() {
        withEnv(javaEnv()) {
            bat 'gradle build'
        }
        archiveArtifacts artifacts: 'build\\libs\\*.war', caseSensitive: false, defaultExcludes: false, excludes: null
    }
}