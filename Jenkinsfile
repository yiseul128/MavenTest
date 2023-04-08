pipeline {
    agent any

    tools {
        maven "maven"
    }

    stages {
        stage('Check out') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/yiseul128/MavenTest.git'
            }
        }
        stage('Build') {
            steps {
                bat "mvn clean package"
            }            
        }
        stage('Test') {
            steps {
                bat "echo running tests"
            }            
        }
        stage('Deliver') {
        	steps('Docker build') {
                bat "docker build -t dew0135/maven-test:%BUILD_ID% ."
            }  
        	steps('Docker login') {
                script {
                    withCredentials([usernamePassword(credentialsId: 'b9385f93-e054-4684-850f-3a9bfa2f2fba', passwordVariable: 'docker_password', usernameVariable: 'docker_username')]) {
                        echo 'Username: %docker_username%'
                        bat 'docker login --username=%docker_username% --password=%docker_password%'
                    }
                }
            }
            steps('Docker push') {
                script {
                    bat 'docker push dew0135/maven-test:%BUILD_ID%'
                }
            }            
        }
    }
}