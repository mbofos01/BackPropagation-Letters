# !/bin/bash

if [ "$#" -ne 1 ] && [ "$#" -ne 2 ]; then
  echo "Usage: $0 parameters_file [clean up flag -C ]" 
  exit 1
fi

echo Running handwritten letter recognition using $1 . . .
javac *.java 
java Network $1
echo Done 
if [[  $2 == '-C'  ]]
then
    echo Cleaning .class files
    rm *.class
fi
