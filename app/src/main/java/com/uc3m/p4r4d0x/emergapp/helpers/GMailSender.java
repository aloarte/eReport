//package com.uc3m.p4r4d0x.emergapp.helpers;
//
///**
// * Created by p4r4d0x on 5/05/16.
// */
//import android.util.Log;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.security.Security;
//import java.util.Properties;
//
//public class GMailSender extends javax.mail.Authenticator{
//    private String mailhost = "smtp.gmail.com";
//    private String user;
//    private String password;
//    private Session session;
//
//    static {
//        Security.addProvider(new JSSEProvider());
//    }
//
//    public GMailSender(String user, String password) {
//        this.user = user;
//        this.password = password;
//
//        Properties props = new Properties();
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.host", mailhost);
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
//        props.put("mail.smtp.port", "465");
//        props.put("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");
//        props.setProperty("mail.smtp.quitwait", "false");
//
//        session = Session.getDefaultInstance(props, this);
//    }
//
//    protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(user, password);
//    }
//
//    public synchronized boolean sendMail(String subject, String body, String sender, String recipients,
//                                      String toSendMessage, String[] toSendPicturesLocation,
//                                      String[] toSendvideosLocation,String toSendGPSCoord,
//                                      String toSendGPSStreet) throws Exception {
//
//        //Build the body message:
//        String bodyMessage=     body            + "\n" +
//                                toSendMessage   + "\n" +
//                                toSendGPSCoord  + "\n" +
//                                toSendGPSStreet + "\n";
//        try {
//
//            // Create a default MimeMessage object.
//            Message message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(sender));
//
//            // Set To: header field of the header.
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(recipients));
//
//            // Set Subject: header field
//            message.setSubject(subject);
//
//            // Create the message part
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            //Set the body of the message
//            messageBodyPart.setText(bodyMessage);
//
//            // Create a multipar message
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//
//            // Set the attached images
//            for(int i=0;i<4;i++){
//                //If the image path was empty, skip it
//                if(toSendPicturesLocation[i].compareTo("")!=0) {
//                    //Create a mime part
//                    messageBodyPart = new MimeBodyPart();
//                    //Get the path of the image
//                    String filename = toSendPicturesLocation[i];
//                    //Open the file in a DataSource
//                    DataSource source = new FileDataSource(filename);
//                    //Set the data source
//                    messageBodyPart.setDataHandler(new DataHandler(source));
//                    //Set its file name
//                    messageBodyPart.setFileName(filename);
//                    //Add it to the main message
//                    multipart.addBodyPart(messageBodyPart);
//                }
//            }
//
//
//            // Set the attached videos
//            for(int i=0;i<4;i++){
//                //If the image path was empty, skip it
//                if(toSendvideosLocation[i].compareTo("")!=0) {
//                    //Create a mime part
//                    messageBodyPart = new MimeBodyPart();
//                    //Get the path of the image
//                    String filename = toSendvideosLocation[i];
//                    //Open the file in a DataSource
//                    DataSource source = new FileDataSource(filename);
//                    //Set the data source
//                    messageBodyPart.setDataHandler(new DataHandler(source));
//                    //Set its file name
//                    messageBodyPart.setFileName(filename);
//                    //Add it to the main message
//                    multipart.addBodyPart(messageBodyPart);
//
//                }
//            }
//
//            //Compose the message with the attached files
//            message.setContent(multipart);
//
//
//            // Send message
//            Transport.send(message);
//            return true;
//
//
//
//        } catch (Exception e) {
//            Log.d("ALR", "Excepcion: "+e);
//            return false;
//        }
//
//
//
//    }
//    public class ByteArrayDataSource implements DataSource {
//        private byte[] data;
//        private String type;
//
//        public ByteArrayDataSource(byte[] data, String type) {
//            super();
//            this.data = data;
//            this.type = type;
//        }
//
//        public ByteArrayDataSource(byte[] data) {
//            super();
//            this.data = data;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getContentType() {
//            if (type == null)
//                return "application/octet-stream";
//            else
//                return type;
//        }
//
//        public InputStream getInputStream() throws IOException {
//            return new ByteArrayInputStream(data);
//        }
//
//        public String getName() {
//            return "ByteArrayDataSource";
//        }
//
//        public OutputStream getOutputStream() throws IOException {
//            throw new IOException("Not Supported");
//        }
//    }
//
//
//}
