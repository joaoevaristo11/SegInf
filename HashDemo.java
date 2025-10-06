import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class HashDemo {

    private static byte[] hashCalculator(String fileName) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        try (FileInputStream msg = new FileInputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int read;
            while((read = msg.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        byte[] h = md.digest();

        return Arrays.copyOfRange(h, 0, 2);
    }

    private static void appendBadApp(String fileName, int count) throws IOException {
        String str = "// Comentario " + count + "\n";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.append(str);
        writer.close();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        String goodFile = "GoodApp.java";
        String badFile = "NewBadApp.java";


        byte[] h16_good = hashCalculator(goodFile);
        byte[] h16_bad = hashCalculator(badFile);

        int count = 0;
        while(!Arrays.equals(h16_good, h16_bad)) {
            if(count == 0) {
                System.out.println("A processar ...");
            }
            appendBadApp(badFile, ++count);
            h16_bad = hashCalculator(badFile);
        }

        System.out.flush();
        if(count==0){
            System.out.println("Os hash codes de ambos os ficheiros já são iguais.");
        }
        System.out.println("Conseguimos! NewBadApp igual a GoodApp, após " + count + " tentativas!");
    }
}