pipeline {
    agent any

    tools {
        maven 'Maven-3.9.16'
    }

    triggers {
        githubPush()
    }

    options {
        buildDiscarder(
            logRotator(numToKeepStr: '10')
        )

        disableConcurrentBuilds()

        timestamps()
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
                echo 'Building the project...'

                sh '''
                    mvn clean install -DskipTests
                '''
            }
        }

        stage('Execute Tests') {
            steps {
                echo 'Executing TestNG test cases...'

                sh '''
                    mvn test
                '''
            }
        }
    }

    post {

        /*
         * This block executes for SUCCESS, FAILURE and UNSTABLE builds.
         * Therefore, reports are published even when test cases fail.
         */
        always {

            echo 'Searching and publishing test reports...'

            /*
             * This helps identify the actual location of the report
             * in the Jenkins Console Output.
             */
            sh '''
                echo "======================================"
                echo "Current Jenkins workspace:"
                pwd

                echo "======================================"
                echo "Searching for Extent Report:"
                find . -type f -name "ExtentReport.html" -print || true

                echo "======================================"
                echo "Searching for Surefire XML reports:"
                find target/surefire-reports \
                    -type f \
                    -name "*.xml" \
                    -print 2>/dev/null || true

                echo "======================================"
            '''

            /*
             * Publish Maven Surefire/TestNG XML results.
             */
            junit(
                testResults: 'target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            /*
             * Publish the Extent HTML Report.
             *
             * Java report location:
             * target/ExtentReport/ExtentReport.html
             */
            publishHTML(target: [
                reportDir             : 'target/ExtentReport',
                reportFiles           : 'ExtentReport.html',
                reportName            : 'Extent Report',
                reportTitles          : 'OrangeHRM Automation Test Report',
                keepAll               : true,
                alwaysLinkToLastBuild : true,
                allowMissing          : true
            ])

            /*
             * Save the Extent Report as a Jenkins build artifact.
             */
            archiveArtifacts(
                artifacts: 'target/ExtentReport/**',
                fingerprint: true,
                allowEmptyArchive: true
            )
        }

        success {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,

                body: """
                    <html>
                        <body>
                            <p>
                                Build Status:
                                <b style="color:green;">SUCCESS</b>
                            </p>

                            <p>
                                Job: ${env.JOB_NAME}<br/>
                                Build Number: ${env.BUILD_NUMBER}<br/>
                                Build URL:
                                <a href="${env.BUILD_URL}">
                                    ${env.BUILD_URL}
                                </a>
                            </p>

                            <p>
                                HTML Report:
                                <a href="${env.BUILD_URL}Extent_Report/">
                                    Open Extent Report
                                </a>
                            </p>

                            <p>
                                Best regards,<br/>
                                Automation Team
                            </p>
                        </body>
                    </html>
                """,

                to: 'kldp2099@gmail.com'
            )
        }

        failure {
            emailext(
                subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,

                body: """
                    <html>
                        <body>
                            <p>
                                Build Status:
                                <b style="color:red;">FAILURE</b>
                            </p>

                            <p>
                                Job: ${env.JOB_NAME}<br/>
                                Build Number: ${env.BUILD_NUMBER}<br/>
                                Build URL:
                                <a href="${env.BUILD_URL}">
                                    ${env.BUILD_URL}
                                </a>
                            </p>

                            <p>
                                HTML Report:
                                <a href="${env.BUILD_URL}Extent_Report/">
                                    Open Extent Report
                                </a>
                            </p>

                            <p>
                                Please check the Jenkins console output
                                and Extent Report for failure details.
                            </p>

                            <p>
                                Best regards,<br/>
                                Automation Team
                            </p>
                        </body>
                    </html>
                """,

                to: 'kldp2099@gmail.com'
            )
        }

        unstable {
            emailext(
                subject: "UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,

                body: """
                    <html>
                        <body>
                            <p>
                                Build Status:
                                <b style="color:orange;">UNSTABLE</b>
                            </p>

                            <p>
                                One or more test cases may have failed.
                            </p>

                            <p>
                                Job: ${env.JOB_NAME}<br/>
                                Build Number: ${env.BUILD_NUMBER}<br/>
                                Build URL:
                                <a href="${env.BUILD_URL}">
                                    ${env.BUILD_URL}
                                </a>
                            </p>

                            <p>
                                HTML Report:
                                <a href="${env.BUILD_URL}Extent_Report/">
                                    Open Extent Report
                                </a>
                            </p>

                            <p>
                                Best regards,<br/>
                                Automation Team
                            </p>
                        </body>
                    </html>
                """,

                to: 'kldp2099@gmail.com'
            )
        }

        cleanup {
            echo 'Cleaning Jenkins workspace...'

            cleanWs()
        }
    }
}