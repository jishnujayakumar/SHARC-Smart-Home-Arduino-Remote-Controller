#include <string.h>
#include <SoftwareSerial.h>
#include <Stepper.h>

//new start
#define RxD 0 // This is the pin that the Bluetooth (BT_TX) will transmit to the Arduino (RxD)
#define TxD 1 // This is the pin that the Bluetooth (BT_RX) will receive from the Arduino (TxD)
#define Door 13


const int stepsPerRevolution = 200;  // change this to fit the number of steps per revolution
char data = 0;               //connect TX to RX and RX to TX
int trigPin = 5;
int echoPin = 6;
int doorcloseflag=0; 

long duration = 0;
int distance = 0;
int automate=9;

SoftwareSerial blueToothSerial(RxD, TxD); // RX, TX
Stepper myStepper(stepsPerRevolution, 7, 8, 9, 10);//stepper pins



//new end
int xflag=0;
int password=LOW;
int passwordPin=2;
int irPin1=3;
char a[2]={'0','0'};
int ir1=LOW;

int irPin2=4;
int ir2=LOW;

void setup() {
  // put your setup code here, to run once:

pinMode(passwordPin,INPUT);
pinMode(irPin1,INPUT);


//new start
  //Serial.begin(9600);//low Serial communication via USB cable to computer (if required)
  pinMode(RxD, INPUT); // Setup the Arduino to receive INPUT from the bluetooth shield on Digital Pin 0
  pinMode(TxD, OUTPUT); // Setup the Arduino to send data (OUTPUT) to the bluetooth shield on Digital Pin 1    
  pinMode(Door, OUTPUT);
  pinMode(12,OUTPUT);
  pinMode(11,OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(A1,INPUT);
  myStepper.setSpeed(60);      
//new end
pinMode(irPin2,INPUT);
Serial.begin(9600);
}



void fan(int x)
{
  int stop = 0;
if(x == 1){
  Serial.println("fan on");
  digitalWrite(11, HIGH);
  myStepper.step(stepsPerRevolution);
   }
if(x == 0){
    Serial.println("fan off");
    digitalWrite(11, LOW);
    myStepper.step(stop);
  }
}

void window(int y)
{
  if(y==1)
  {
  // step one revolution  in one direction:
  Serial.println("opening");
  myStepper.step(stepsPerRevolution);
  delay(500);
  }
  if(y==0)
  {
    Serial.println("closing");
  myStepper.step(-stepsPerRevolution);
  delay(500);
  }
 }
void curtain(int x){
if (x == 1){
    Serial.println("curtain OPEN");
    digitalWrite(11, HIGH);
    myStepper.step(stepsPerRevolution);
}
if (x == 0){
  // step one revolution n the other direction:
    Serial.println("curtain CLOSE");
    digitalWrite(11, LOW); 
 //  myStepper.step(-stepsPerRevolution);
}
}

void temperature(){
  int val = analogRead(A1);

float cel = ( val/1024.0)*500; 

float farh = (cel*9)/5 + 32;

Serial.print("TEMPRATURE = ");
Serial.print(cel);

if(cel > 42){
  
  Serial.print("DANGER : TEMPERATURE IS VERY HIGH.");
  digitalWrite(2,HIGH);
  
  }else{
    Serial.print("TEMPERATURE IS BEARABLE.");
    digitalWrite(2,LOW);
    }

  }
void water_level(){
    
    digitalWrite(trigPin, LOW);
    delayMicroseconds(10000);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10000);
    
    digitalWrite(trigPin, LOW);
    
    duration = pulseIn(echoPin, HIGH);
    distance= duration*0.034/2;
    if(distance<3){
      Serial.println("Overflow");
      digitalWrite(2,HIGH);
     }
    else if(distance>7)
    {
      Serial.println("Refill");
      digitalWrite(2,LOW);
    }
}

void light(int x){
 
    if(x==1){
    digitalWrite(12, HIGH); }
    else if(x==0)
    {
      digitalWrite(12,LOW);
    }
}

int door(){
  int flag=0;
  if(ir1==HIGH && ir2==LOW){
    //delay(2000);
      if(a[0]=='0'){
        a[0]='1';
        Serial.print(a[0]);
        Serial.print(a[1]);
        }
        if(a[0]=='2'){
          a[1]='1';
          
        Serial.print(a[0]);
        Serial.print(a[1]);
          }

    
    }
  
    if( ir1==LOW && ir2==HIGH){
      if(a[0]=='0'){
        a[0]='2';
        
        Serial.print(a[0]);
        Serial.print(a[1]);
        }
        
        if(a[0]=='1'){
          a[1]='2';
          
        Serial.print(a[0]);
        Serial.print(a[1]);
          }   
      }
      Serial.print(a[0]);
        Serial.print(a[1]);
       

  if(a[0]=='1'  && a[1]=='2'){
  Serial.println("The person has entered");  
    a[0]='0';
    a[1]='0';
    flag=1;
    delay(1000);
    }
  if(a[0]=='2'  && a[1]=='1'){
  Serial.println("The person has gone out");  
    a[0]='0';
    a[1]='0';
    flag=2;
    delay(1000);
    }
    /*
    if(a[0]=='1'  && a[1]=='1'){
  //Serial.println("The person has gone out");  
    a[0]='0';
    a[0]='0';
    
    }
    if(a[0]=='2'  && a[1]=='2'){
  //Serial.println("The person has gone out");  
    a[0]='0';
    a[0]='0';
    
    }
 */
  
    return flag;
  }


void loop() {


   if( Serial.available() )       // if data is available to read
  {
    data = Serial.read();         // read it and store it in 'val'
  }

  switch (data){
    
    case '0' : digitalWrite(13, LOW);break;
    case '1' : digitalWrite(13, HIGH);break;
    case '2' : fan(1);break;
    case '3' : fan(0);break;
    case '4' : light(1);Serial.print("lights onnnnnnnnn");break;
    case '5' : light(0);Serial.print("lights OFFFFFFFFFFF");break;
    case '6' : window(1);break;
    case '7' : window(0);break;
    case '8' : automate=8;break;
    case '9' : automate=9;break;
    
    }

  if(automate==8){
    
  if(0== 0)
  {
    //data = Serial.read(); 
    //  data=1;
    Serial.print(data);  
    Serial.print("\n"); 
    
    if(data == '1' || doorcloseflag==1)  {    //it is reading '1' means the ascii value of 1 which is 49
  
    doorcloseflag=1;
      digitalWrite(13, HIGH);
    //  blueToothSerial.print(data);
      Serial.print("Door is open\n"); 
    ir1=digitalRead(irPin1);
  //
  ir2=digitalRead(irPin2);
        xflag=door();
        if(xflag==1){
          fan(1);
          window(1);
          
          light(1);
        //curtain(1);
       
          }
          if(xflag==2){
            fan(0);
            
            window(0);
            light(0);
            
        //curtain(0);
       
            }    
  }
  
    if(data == '0' || doorcloseflag==0){ //it same as above 0 ascii 48
      doorcloseflag=0;
      digitalWrite(13, LOW);   
     // blueToothSerial.print(data);
      Serial.print("Door is closed\n"); 
       ir1=digitalRead(irPin1);
  //
  ir2=digitalRead(irPin2);
        xflag=door();
        if(xflag==1){
          fan(1);
          light(1);
        //curtain(1);
       
          }
          if(xflag==2){
            fan(0);
            light(0);
        //curtain(0);
       
            }
     
     //   fan(0);
     //   curtain(0);
    
    
    
    }
    
    
    }
    }else if(automate==9){


      while( Serial.available() > 0 && Serial.read() != 8){       // if data is available to read

  switch (data){
    
    case '0' : digitalWrite(13, LOW);break;
    case '1' : digitalWrite(13, HIGH);break;
    case '2' : fan(1);break;
    case '3' : fan(0);break;
    case '4' : light(1);break;
    case '5' : light(0);break;
    case '6' : window(1);break;
    case '7' : window(0);break;    
    }
      }
    }
  
  water_level();
            temperature();
 //  Serial.print("main loop");
  delay(1000);
  
 /* // put your main code here, to run repeatedly:
password=digitalRead(passwordPin);
if(password==HIGH){
  Serial.println("Password Correct");
   ir1=digitalRead(irPin1);
  //
  ir2=digitalRead(irPin2);
 
  door();
  
  }

  
delay(700);
*/
}
