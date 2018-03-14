import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class BMPReader {
    public static int[][] readBMP(String src) throws IOException {
        FileInputStream fis = new FileInputStream(src);

        //把数组定义为int[28][28]
        int bitArray[] = new int[28 * 28];
        int[][] data = new int[28][28];

        fis.skip(54);
        fis.skip(8);
        int temp = 0;
        int count = 0;
        for (int i = 0; i < 32 * 28 / 8; i++) {
            temp = fis.read();

            //跳过补0
            if (i % 4 == 3) {
                for (int j = 7; j >= 4; j--) {
                    bitArray[count] = (temp >> j) & 0x1;
                    count++;
                }
            } else {
                for (int j = 7; j >= 0; j--) {
                    bitArray[count] = (temp >> j) & 0x1;
                    count++;
                }
            }

        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++)
                data[data.length - 1 - i][j] = bitArray[28 * i + j];
        }

        fis.close();
        return data;
    }

    public static double[] aToV(String src) throws IOException {
        int len = 7;
        int step = 28 / len;
        double[] vector = new double[len * len];
        int data[][] = readBMP(src);
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                int in[][] = new int[step][step];
                for (int k = 0; k < step; k++) {
                    for (int m = 0; m < step; m++) {
                        in[k][m] = data[i * step + k][j * step + m];
                    }
                }
                int num = getNum(in);
                vector[i * len + j] = num;
            }
        }
        return vector;
    }

    private static int getNum(int[][] in) {
        int num = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] == 0)
                    num++;
            }
        }
        return num;
    }


    public static void main(String[] args) throws IOException {
        int data[][] = readBMP("Train/8/2.bmp");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++)
                System.out.print(data[i][j]);
            System.out.println();
        }
        double out[] = aToV("Train/8/2.bmp");
        System.out.println(Arrays.toString(out));
    }

}
