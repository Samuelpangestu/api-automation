pipeline {
    agent any

    parameters {
        choice(
            name: 'TEST_TAG',
            choices: ['@smoke', '@positive', '@negative', '@all'],
            description: 'Select test tag to run'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['production', 'staging', 'development'],
            description: 'Target environment'
        )
    }

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        ALLURE_RESULTS = 'target/allure-results'
        ALLURE_REPORT = 'target/allure-report'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checking out API automation code..."
                checkout scm
            }
        }

        stage('Setup') {
            steps {
                echo "Java version:"
                sh 'java -version'
                echo "Maven version:"
                sh 'mvn -version'
            }
        }

        stage('Clean') {
            steps {
                echo "Cleaning previous build..."
                dir('api-automation') {
                    sh 'mvn clean'
                }
            }
        }

        stage('Compile') {
            steps {
                echo "Compiling project..."
                dir('api-automation') {
                    sh 'mvn compile test-compile'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def tag = params.TEST_TAG == '@all' ? '' : "-Dcucumber.filter.tags='${params.TEST_TAG}'"
                    def env = params.ENVIRONMENT

                    echo "Running tests with tag: ${params.TEST_TAG}"
                    echo "Environment: ${env}"

                    dir('api-automation') {
                        sh """
                            mvn test ${tag} -Denv=${env} || true
                        """
                    }
                }
            }
        }

        stage('Generate Report') {
            steps {
                echo "Generating Allure report..."
                dir('api-automation') {
                    script {
                        allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                        ])
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Test execution completed"
            dir('api-automation') {
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            }
        }
        success {
            echo "API tests passed successfully!"
        }
        failure {
            echo "API tests failed. Check the report for details."
        }
    }
}
