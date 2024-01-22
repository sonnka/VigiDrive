pipeline {
	agent any

	environment {
		mavenHome = tool 'jenkins_maven'
	}

	tools {
		jdk 'JDK-17'
	}

	stages {

		stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}

        stage('Build Docker Image of DB') {
            steps {
                bat 'docker-compose -f docker-compose.yml up -d --build'
                bat 'timeout 20'
            }
        }

		stage('Test'){
			steps{
				bat "mvn test"
			}
		}
	}
}