pipeline {

    agent any
    parameters {
        booleanParam(name: 'INSTALL_POSTGRES', defaultValue: true, description: 'Install PostgreSQL')
    }

    environment {
            PATH = "/usr/bin:/usr/local/bin:${env.PATH}"
        }


    stages {

//         stage('Cleanup Workspace') {
//                     steps {
//                         cleanWs()
//                     }
//                 }
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
             when {
                expression { return params.INSTALL_POSTGRES }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appservers ~/workspace/ansible/playbook/spring-docker.yaml
                '''
            }
        }


}
}