import java.io.IOException;

public class ReadCharacter {
    public static void main(String args[]) throws IOException {
        long startTime = System.currentTimeMillis();
        //初始化神经网络的基本配置

        int nn = 150;
        BP bp = new BP(new int[]{7 * 7, nn, nn, nn, 14}, 0.4, 0.8);
        double data[][][] = new double[14][128][];

        BMPReader bmpReader = new BMPReader();

        int corr = 0;
        int total = 0;
        for (int n = 0; n < 1000; n++) {
            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 14; j++) {
                    double[] result = new double[14];
                    for (int k = 0; k < result.length; k++)
                        result[k] = 0;
                    result[j] = 1;
                    data[j][i] = bmpReader.aToV("Train/" + (j + 1) + "/" + i + ".bmp");
                    bp.train(data[j][i], result, 1, 0);
                }

            }
            for (int i = 0; i < 14; i++) {
                for (int j = 128; j < 192; j++) {

                    double[][][] tdata = new double[14][64][];
                    tdata[i][j - 128] = bmpReader.aToV("Train/" + (i + 1) + "/" + j + ".bmp");

                    double[] res = bp.forward(tdata[i][j - 128], 1, 0);

                    //for (int k = 0; k < res.length; k++)
                    //System.out.print(res[k] + " ");
                    //System.out.println();
                    int max = bp.getMax(res);
                    total++;
                    if (i == max)
                        corr++;
                    //System.out.println(i + ": " + max);
                }

            }
            //System.out.println("c:" + corr + " t:" + total);
            double r = (double) corr / (double) total;
            System.out.println(r);
            if (r > 0.83) {
                System.out.println("n=" + n);
                break;
            }

        }

        for (int i = 0; i < 14; i++) {
            for (int j = 192; j < 256; j++) {

                double[][][] tdata = new double[14][64][];
                tdata[i][j - 192] = bmpReader.aToV("Train/" + (i + 1) + "/" + j + ".bmp");

                double[] res = bp.forward(tdata[i][j - 192], 1, 0);

                //double[] res = bp.forward(data[i][j]);

                //System.out.print(i+": ");
                /*for (int k = 0; k < res.length; k++)
                    System.out.print(res[k] + " ");
                System.out.println();*/
                int max = bp.getMax(res);
                total++;
                if (i == max)
                    corr++;
            }

        }
        double r = (double) corr / (double) total;
        System.out.println(r);


        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime) / 1000;
        System.out.println("程序运行时间： " + time + "s");
    }

}
