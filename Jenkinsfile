pipeline {
    agent any

    tools {
        maven 'Maven-3.9.16'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/coolkd/Selenium-Test-Framework.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Publish Reports') {
            steps {
                junit 'target/surefire-reports/*.xml'

                publishHTML([
                    reportDir: 'src/test/resources/ExtentReport',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

                archiveArtifacts artifacts: 'src/test/resources/ExtentReport/*.html', fingerprint: true
            }
        }
    }

    post {
        success {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """<p>Build Status: <b>SUCCESS</b></p>
                         <p>Job: ${env.JOB_NAME}<br/>
                         Build Number: ${env.BUILD_NUMBER}<br/>
                         Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                         <p>HTML Report: <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>
                         <br/>
                         <p>Best regards,<br/>Automation Team</p>""",
                to: 'kldp2099@gmail.com'
            )
        }
        failure {
            emailext(
                subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """<p>Build Status: <b>FAILURE</b></p>
                         <p>Job: ${env.JOB_NAME}<br/>
                         Build Number: ${env.BUILD_NUMBER}<br/>
                         Build URL: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                         <p>HTML Report: <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>
                         <br/>
                         <p>Best regards,<br/>Automation Team</p>""",
                to: 'kldp2099@gmail.com'
            )
        }
        always {
            cleanWs()
        }
    }
}
