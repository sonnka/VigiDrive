pipeline {
	agent any

	environment {
		mavenHome = tool 'jenkins_maven'
	}

	tools {
		jdk 'JDK-17'
	}
// Test Jenkins
	stages {
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