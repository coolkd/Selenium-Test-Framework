stage('Publish Reports') {
    steps {
        // Publish JUnit XML results
        junit 'target/surefire-reports/*.xml'

        // Publish ExtentReport HTML with required parameters
        publishHTML([
            reportDir: 'src/test/resources/ExtentReport',
            reportFiles: 'ExtentReport.html',
            reportName: 'Extent Report',
            keepAll: true,                  // keep reports for all builds
            alwaysLinkToLastBuild: true,    // link report to latest build
            allowMissing: false             // fail if report is missing
        ])

        // Archive artifacts so they appear in Jenkins UI
        archiveArtifacts artifacts: 'src/test/resources/ExtentReport/*.html', fingerprint: true
    }
}
