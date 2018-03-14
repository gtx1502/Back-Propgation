
public class Sin {
    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();

        int inParts = 4000;
        int nn = 20;
        int tot = 0;
        int cor = 0;
        double ra;
        BP bp = new BP(new int[]{1, nn, nn, nn, 1}, 0.025, 0.85);
        for (int n = 0; n < 30000; n++) {
            double[][] in = new double[inParts][1];
            double[][] target = new double[inParts][1];
            for (int i = 0; i < inParts; i++) {
                double x0 = -Math.PI + i * 2 * Math.PI / inParts;
                in[i][0] = x0;
                double y = getSin(x0);
                target[i][0] = (y + 1) / 2;
                bp.train(in[i], target[i], 3, -2);
            }
            double tX = Math.random() * 2 * Math.PI - Math.PI;
            double test[] = new double[1];
            test[0] = tX;
            double[] result = bp.forward(test, 3, -2);
            double tY = result[0] * 2 - 1;
            //double tY=result[0];
            double d = getSin(tX) - tY;
            if (d < 0)
                d = -d;
            if (d < 0.01)
                cor++;
            tot++;
            ra = (double) cor / (double) tot;
            if (ra > 0.5)
                break;
            //d=d/getSin(tX);
            System.out.println(ra + "   " + d + "     " + tX + ":" + getSin(tX) + " " + tY);
        }
        double total = 0;
        for (int i = 0; i < 1000; i++) {
            double tX = Math.random() * 2 * Math.PI - Math.PI;
            double test[] = new double[1];
            test[0] = tX;
            double[] result = bp.forward(test, 3, -2);
            double tY = result[0] * 2 - 1;
            //double tY = result[0];

            double d = getSin(tX) - tY;
            if (d < 0)
                d = -d;
            if (d < 0.01)
                cor++;
            tot++;
            ra = (double) cor / (double) tot;
            System.out.println(ra + "   " + d + "     " + tX + ":" + getSin(tX) + " " + tY);
            total = total + d;
        }
        double average = total / 1000;
        System.out.println("average:" + average);
        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime) / 1000;
        System.out.println("程序运行时间： " + time + "s");


    }

    private static double getSin(double in) {
        return Math.sin(in);
    }
}
