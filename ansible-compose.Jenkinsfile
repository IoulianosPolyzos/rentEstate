pipeline {

    agent any
    parameters {
        booleanParam(name: 'INSTALL', defaultValue: true, description: 'Install')
    }

    environment {
            PATH = "/usr/bin:/usr/local/bin:${env.PATH}"
        }


    stages {

        stage('Checkout Ansible repo') {
                    steps {
                        checkout([$class: 'GitSCM', branches: [[name: 'main']],
                            userRemoteConfigs: [[url: 'https://github.com/IoulianosPolyzos/ansible.git']]])
                    }
                }

        stage('test connection to deploy env') {
        steps {
            sh '''
                ansible -i ~/workspace/ansible-compose/hosts.yaml -m ping appservers,dbservers
            '''
            }
        }

        stage('Install') {
             when {
                expression { return params.INSTALL }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible-compose/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible-compose/hosts.yaml -l appservers ~/workspace/ansible-compose/playbook/spring-docker.yaml
                '''
            }
        }


}
}