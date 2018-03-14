import java.util.Random;

public class BP {
    private double[][] node;//各层节点
    private double[][] err;//各节点误差
    private double[][][] weight;//各节点的权重
    private double[][][] increase;//各节点权重调整量
    private double mc;//动量因子
    private double rate;//learning rate

    /**
     * @param nodeNum 每层节点数
     * @param rate    learning rate
     * @param mc      动量因子
     */
    public BP(int[] nodeNum, double rate, double mc) {
        this.mc = mc;
        this.rate = rate;
        node = new double[nodeNum.length][];
        err = new double[nodeNum.length][];
        weight = new double[nodeNum.length][][];
        increase = new double[nodeNum.length][][];
        Random random = new Random();
        for (int i = 0; i < nodeNum.length; i++) {
            node[i] = new double[nodeNum[i]];
            err[i] = new double[nodeNum[i]];
            if (i + 1 < nodeNum.length) {     //存在下一层神经元
                weight[i] = new double[nodeNum[i] + 1][nodeNum[i + 1]];           //nodeNum[i]+1 多出的1个表示截距
                increase[i] = new double[nodeNum[i] + 1][nodeNum[i + 1]];
                for (int j = 0; j < nodeNum[i] + 1; j++)
                    for (int k = 0; k < nodeNum[i + 1]; k++) {
                        double d = 0.3 * random.nextDouble();//随机初始化到达下层第k个节点的权重
                        weight[i][j][k] = random.nextDouble() > 0.5 ? d : -d;
                    }
            }
        }
    }

    /**
     * sigmoidal function
     *
     * @param in 权重
     * @return
     */
    private double sigmoid(double in) {
        return 1 / (1 + Math.exp(-in));
    }

    /**
     * 逐层向前计算输出
     *
     * @param in 输入层
     * @return 输出层
     */
    public double[] forward(double[] in, int m, int n) {
        for (int i = 0; i < node.length - 1; i++) {
            for (int j = 0; j < node[i + 1].length; j++) {
                double w = weight[i][node[i].length][j];          //截距
                for (int k = 0; k < node[i].length; k++) {
                    if (i == 0)        //输入层
                        node[i][k] = in[k];
                    w += weight[i][k][j] * node[i][k];  //计算权重
                }
                node[i + 1][j] = sigmoid(w) * m + n;    //激活函数
            }
        }
        return node[node.length - 1];
    }

    /**
     * 逐层反向计算误差并修改权重
     *
     * @param tar 预期值数组
     */
    private void backward(double[] tar, int m, int n) {
        int i = node.length - 1;
        double nodeErr;     //单个node的误差
        for (int j = 0; j < err[i].length; j++) {
            nodeErr = tar[j] - node[i][j];
            err[i][j] = 1 / (double) m * (node[i][j] - n) * (1 - node[i][j]) * nodeErr;  //计算输出层误差
        }

        for (i = node.length - 2; i > 0; i--) {
            for (int j = 0; j < err[i].length; j++) {
                nodeErr = 0;
                for (int k = 0; k < err[i + 1].length; k++) {
                    nodeErr += err[i + 1][k] * weight[i][j][k];
                    increase[i][j][k] = mc * increase[i][j][k] + (1 - mc) * rate * err[i + 1][k] * node[i][j];//hidden层动量调整
                    weight[i][j][k] += increase[i][j][k];//hidden层权重调整
                    if (j == err[i].length - 1) {       //截距
                        increase[i][j + 1][k] = mc * increase[i][j + 1][k] + (1 - mc) * rate * err[i + 1][k];//截距动量调整
                        weight[i][j + 1][k] += increase[i][j + 1][k];//截距权重调整
                    }
                }
                err[i][j] = 1 / (double) m * nodeErr * (node[i][j] - n) * (1 - node[i][j]);//计算误差
            }
        }

    }

    public void train(double[] in, double[] tar, int m, int n) {
        forward(in, m, n);
        backward(tar, m, n);
    }

    public static int getMax(double[] in) {
        double max = in[0];
        int num = 0;
        for (int i = 1; i < in.length; i++) {
            if (in[i] > max) {
                max = in[i];
                num = i;
            }
        }
        return num;
    }
}
