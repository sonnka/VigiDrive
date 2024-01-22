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

		stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}

        stage('Build Docker Image of DB') {
                steps {
                    bat 'docker-compose -f docker-compose.yml run'
                }
        }

		stage('Test'){
			steps{
				bat "mvn test"
			}
		}
	}
}