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
                bat 'mvn clean package'
            }            
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }            
        }
        stage('Code Coverage') {
        	steps {
        	    jacoco()
        	}
		}
        stage('Code Static Analysis') {
        	steps {
        	    bat 'mvn pmd:pmd checkstyle:checkstyle spotbugs:spotbugs -X'
        	}
		}
        stage('Deliver') {
        	steps {
        		script {
                    bat 'docker build -t dew0135/maven-test:%BUILD_ID% .'
                }
                
                
                script {
                    withCredentials([usernamePassword(credentialsId: 'b9385f93-e054-4684-850f-3a9bfa2f2fba', passwordVariable: 'docker_password', usernameVariable: 'docker_username')]) {
                        echo 'Username: %docker_username%'
                        bat 'docker login --username=%docker_username% --password=%docker_password%'
                    }
                }

                script {
                    bat 'docker push dew0135/maven-test:%BUILD_ID%'
                }
            }            
        }
        stage('Deploy to DEV') {
            steps {
                script {
                	bat '''
						for /f %%i in ('docker ps -qf "name=^maven-test"') do set containerId=%%i
						echo %containerId%
						If "%containerId%" == "" (
						  echo "No Container running"
						) ELSE (
						  docker stop %ContainerId%
						  docker rm -f %ContainerId%
						)
						'''

                    docker.image("dew0135/maven-test:${BUILD_ID}").run("-p 8081:8081 --name maven-test")
                }
            }
        }
    }
}