package net.skhu.mentoring.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Mail {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        sendEmail("wkdtndgns@naver.com");
    }


    public static void sendEmail(String email) throws IOException {
        // 네이버일 경우 smtp.naver.com 을 입력합니다. // Google일 경우 smtp.gmail.com 을 입력합니다.
        Properties mailAdmistrator=new Properties();
        String path =Mail.class.getResource("").getPath();
        FileInputStream fis=new FileInputStream(path+"mailAdmistrator.properties");
        mailAdmistrator.load(fis);

        String host = mailAdmistrator.getProperty("host");
        final String username = mailAdmistrator.getProperty("username");
        final String password = mailAdmistrator.getProperty("password");
        int port= Integer.valueOf(mailAdmistrator.getProperty("port")); //포트번호
        // 메일 내용


        String recipient = email;
        //받는 사람의 메일주소를 입력해주세요.
        String subject = "인증 번호 발송 "; //메일 제목 입력해주세요.
        String body = "관리자로 부터 메일을 받았습니다. "
                + "	     인증 번호는: ";
        //메일 내용 입력해주세요.

        try {
            Properties props = System.getProperties();
            // 정보를 담기 위한 객체 생성
            // SMTP 서버 정보 설정
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.debug", "true");

            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", host); //Session 생성
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");


            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
            { String un=username; String pw=password; @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication()
            { return new javax.mail.PasswordAuthentication(un, pw); } }); session.setDebug(true); //for debug
            Message mimeMessage = new MimeMessage(session); //MimeMessage 생성
            mimeMessage.setFrom(new InternetAddress("wkdtndgns@naver.com")); //발신자 셋팅 , 보내는 사람의 이메일주소를 한번 더 입력합니다.이때는 이메일 풀 주소를 다 작성해주세요.
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); //수신자셋팅 //.TO 외에 .CC(참조) .BCC(숨은참조) 도 있음
            mimeMessage.setSubject(subject); //제목셋팅
            mimeMessage.setText(body); //내용셋팅
            Transport.send(mimeMessage);
            //javax.mail.Transport.send() 이용
        }

        catch(MessagingException e) {
            e.printStackTrace();


        }finally {
            fis.close();
        }

    }
}