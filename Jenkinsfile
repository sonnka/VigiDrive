pipeline {
	agent any

	environment {
		mavenHome = tool 'jenkins_maven'
	}

	tools {
		jdk 'JDK-17'
	}
// Test 3 Jenkins
	stages {

	  stage('Build Docker Image of DB') {
            steps {
                bat './docker-compose.yml'
            }
      }

		stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}

		stage('Test'){
			steps{
				bat "mvn test"
			}
		}
	}
}