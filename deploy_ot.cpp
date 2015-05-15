#include <sys/types.h>
#include <sys/stat.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <syslog.h>
#include <string.h>

#include <ctime>
#include <iostream>
#include <string>
#include <cstdio>
#include <iostream>
#include <fstream>
using namespace std;

// Exec function                                                                                                                                                                                     
std::string exec(const char* cmd) {
  FILE* pipe = popen(cmd, "r");
  if (!pipe) return "ERROR";
  char buffer[128];
  std::string result = "";
  while(!feof(pipe)) {
    if(fgets(buffer, 128, pipe) != NULL)
      result += buffer;
  }
  pclose(pipe);
  return result;
}

int main(void) {

  // init addrs                                                                                                                                                                                    
  static const string address[] = {
      "54.84.106.22",   // N virginia           YogiBear1
      "52.8.114.154",   // N California         YogiBear2
      "54.66.200.133",  // Sydney               YogiBear3
      "52.74.22.123",   // Singapore            YogiBear4
      "54.207.93.174",  // Sao Paulo            YogiBear5
      "52.17.225.122"   // Ireland (log site)   YogiBear6
      
  };

  // scp -r -i ~/Desktop/turtlebeards.pem ~/Dropbox/Current\ Documents/cs271/cs271_proj1_java/clientServer/dist/clientServer.jar ec2-user@54.174.167.183:/home/ec2-user/                           
  //for(int i = 0; i < 5; i++) {
  string cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_Virginia.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[0];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  string result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_Virginia.pem ec2-user@"+address[0]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
    
  cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_California.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[1];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_California.pem ec2-user@"+address[1]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
    
  cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_Sydney.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[2];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_Sydney.pem ec2-user@"+address[2]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
    
  cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_Singapore.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[3];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_Singapore.pem ec2-user@"+address[3]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
    
  cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_SaoPaolo.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[4];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_SaoPaolo.pem ec2-user@"+address[4]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());

  cmd = "scp -r -i ~/Desktop/Pem_Keys/YogiBearKey_Ireland.pem /Users/olivertownsend/Documents/Classes/4.\ Senior\ Year/3.\ Spring/171/project3/CS171_Project3/Project3.jar ubuntu@"+address[5];
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
  cout << "> DELETING LOGS" << endl;
  cmd = "ssh -i /Users/olivertownsend/Desktop/Pem_Keys/YogiBearKey_Ireland.pem ec2-user@"+address[5]+" 'rm log.txt'";
  cout.write(cmd.c_str(), strlen(cmd.c_str()));
  cout.put('\n');
  result = exec(cmd.c_str());
      
      
  // sleep(1);                                                                                                                                                                                 
  //}
    

  exit(EXIT_SUCCESS);
}
