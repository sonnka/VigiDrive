pipeline {
	agent any

	stages {

        stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}
        stage('Build Docker') {
            steps {
                bat 'docker-compose -f docker-compose.yml up -d --build'
            }
        }
	}
}