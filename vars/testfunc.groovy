def call(String name = 'human') {
  
    def _s = name
    echo "Hello, ${name}."
    sh 'npm install'
}