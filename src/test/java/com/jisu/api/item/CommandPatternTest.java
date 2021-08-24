package com.jisu.api.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandPatternTest {

    //커맨드 객체
    public interface Command {
        // 커맨드 객체는 execute만을 지원
        // 추가 구현시 undo 메소드로 되돌리기 기능 구현 가능
        public void execute();
    }

    //리시버 객체
    public class Aircon {
        public void on(){
            System.out.println("turn on");
        }
        public void off(){
            System.out.println("turn off");
        }
        public void increase(){
            System.out.println("increase temp");
        }
        public void decrease(){
            System.out.println("decrease temp");
        }
    }

    //인터페이스를 상속받는 커맨드 객체 생성
    //리시버가 가지고 있는 기능에 따라 여러 구현체를 만들 수 있다.
    public class TurnOnCommand implements Command{
        Aircon aircon;
        public TurnOnCommand(Aircon aircon){
            this.aircon = aircon;
        }

        // 필수 구현, 리시버의 실제 메서드를 실행시킴
        @Override
        public void execute(){
            aircon.on();
        }
    }
    public class TurnOffCommand implements Command{
        Aircon aircon;
        public TurnOffCommand(Aircon aircon){
            this.aircon = aircon;
        }

        @Override
        public void execute(){
            aircon.off();
        }
    }

    //인보커 객체
    //커맨드 객체를 저장. 클라이언트 객체에서 buttonPressed 메소드를 호출하면
    //저장된 커맨드 객체메소드를 호출한다.
    public class SimpleRemoteControl {
        Command remote;
        public SimpleRemoteControl(){}

        // 클라이언트는 setCommand의 인자로
        // command의 구현체인 xxCommand를 넣어줄 것이다.
        public void setCommand(Command command){
           remote = command;
        }
        public void buttonPressed(){
            remote.execute();
        }
    }

    @Test
    @DisplayName("클라이언트 객체 생성 및 turn on 테스트")
    public void test(){
        // invoker
        SimpleRemoteControl remote = new SimpleRemoteControl();
        // receiver
        Aircon aircon = new Aircon();
        // command
        TurnOnCommand turnOnCommand = new TurnOnCommand(aircon);
        TurnOffCommand turnOffCommand = new TurnOffCommand(aircon);

        // turn on
        remote.setCommand(turnOnCommand);
        remote.buttonPressed();

        remote.setCommand(()-> aircon.increase());
        remote.buttonPressed();
        remote.buttonPressed();
        remote.setCommand(()-> aircon.decrease());
        remote.buttonPressed();

        // turn off
        remote.setCommand(turnOffCommand);
        remote.buttonPressed();
    }
}
