pipeline {

    agent any

    stages {
    
        stage('run ansible pipeline') {
            steps {
                build job: 'ansible'
            }
        }

	stage('Checkout Ansible repo') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: 'main']],
                   userRemoteConfigs: [[url: 'https://github.com/IoulianosPolyzos/ansible.git']]])
            }
        }



        stage('test connection to deploy env') {
        steps {
            sh '''
                ansible -i ~/workspace/ansible/hosts.yaml -m ping appservers,dbservers
            '''
            }
        }
        
        stage('Install postgres') {
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appservers ~/workspace/ansible/playbook/spring-docker.yaml
                '''
            }
        }

}
}
