import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class solution {

    public static class imu {
        List<String> imuBuf;
        double ACC_X = 0.0, ACC_Y = 0.0, ACC_Z = 0.0, GYR_X = 0.0, GYR_Y = 0.0, GYR_Z = 0.0;
        double MAG_X = 0.0, MAG_Y = 0.0, MAG_Z = 0.0, PITCHX = 0.0, ROLLY = 0.0, YAWZ = 0.0, PRES = 0.0;
        int ACCE_NUM, GYRO_NUM, MAGN_NUM, PRES_NUM, AHRS_NUM;
        int dataTypeNum;
        double SensorTimeStamp;
        String path;

        imu(String path) {
            imuBuf = new ArrayList<>();
            this.path = path;
            imuBuf = new ArrayList<>();
            clean();// 数据初始化
        }

        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < imuBuf.size(); i++) {
                    bw.write(imuBuf.get(i));
                    // System.out.println("imuBuf["+i+"]:\t"+imuBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imuBuf.clear();
        }

        public void process(String strs[]) {
            switch (strs[0]) {
            case "ACCE":
                ACC_X += Double.parseDouble(strs[3]);
                ACC_Y += Double.parseDouble(strs[4]);
                ACC_Z += Double.parseDouble(strs[5]);
                if (ACCE_NUM == 0) {
                    dataTypeNum++;
                }
                ACCE_NUM++;
                break;
            case "GYRO":
                GYR_X += Double.parseDouble(strs[3]);
                GYR_Y += Double.parseDouble(strs[4]);
                GYR_Z += Double.parseDouble(strs[5]);
                if (GYRO_NUM == 0) {
                    dataTypeNum++;
                }
                GYRO_NUM++;
                break;
            case "PRES":
                PRES += Double.parseDouble(strs[3]);
                if (PRES_NUM == 0) {
                    dataTypeNum++;
                }
                PRES_NUM++;
                break;
            case "AHRS":
                PITCHX += Double.parseDouble(strs[3]);
                ROLLY += Double.parseDouble(strs[4]);
                YAWZ += Double.parseDouble(strs[5]);
                if (AHRS_NUM == 0) {
                    dataTypeNum++;
                }
                AHRS_NUM++;
                break;
            case "MAGN":
                MAG_X += Double.parseDouble(strs[3]);
                MAG_Y += Double.parseDouble(strs[4]);
                MAG_Z += Double.parseDouble(strs[5]);
                if (MAGN_NUM == 0) {
                    dataTypeNum++;
                }
                MAGN_NUM++;
                break;
            }
            SensorTimeStamp = Math.min(SensorTimeStamp, Double.parseDouble(strs[2]));
            if (dataTypeNum == 5) {
                StringBuilder sb = new StringBuilder();
                sb.append(Double.toString((Double.parseDouble(strs[1]) - SensorTimeStamp) * 1000) + ",");
                sb.append(Double.toString(ACC_X / ACCE_NUM) + "," + Double.toString(ACC_Y / ACCE_NUM) + ","
                        + Double.toString(ACC_Z / ACCE_NUM) + ",");
                sb.append(Double.toString(GYR_X / GYRO_NUM) + "," + Double.toString(GYR_Y / GYRO_NUM) + ","
                        + Double.toString(GYR_Z / GYRO_NUM) + ",");
                sb.append(Double.toString(MAG_X / MAGN_NUM) + "," + Double.toString(MAG_Y / MAGN_NUM) + ","
                        + Double.toString(MAG_Z / MAGN_NUM) + ",");
                sb.append(Double.toString(PITCHX / AHRS_NUM) + "," + Double.toString(ROLLY / AHRS_NUM) + ","
                        + Double.toString(YAWZ / AHRS_NUM) + ",");
                sb.append(Double.toString(PRES));
                imuBuf.add(sb.toString());
                clean();
                if (imuBuf.size() == 100)
                    writeFile();
            }
        }

        // 初始化
        public void clean() {
            ACC_X = 0.0;
            ACC_Y = 0.0;
            ACC_Z = 0.0;
            GYR_X = 0.0;
            GYR_Y = 0.0;
            GYR_Z = 0.0;
            MAG_X = 0.0;
            MAG_Y = 0.0;
            MAG_Z = 0.0;
            PITCHX = 0.0;
            ROLLY = 0.0;
            YAWZ = 0.0;
            PRES = 0.0;
            ACCE_NUM = GYRO_NUM = MAGN_NUM = AHRS_NUM = PRES_NUM = 0;
            dataTypeNum = 0;
            SensorTimeStamp = Double.MAX_VALUE;
        }
    }

    public static class wifi {
        List<String> wifiBuf;
        double SensorTimeStamp;
        String path;
        int count = 1;

        wifi(String path) {
            wifiBuf = new ArrayList<>();
            this.path = path;
        }

        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < wifiBuf.size(); i++) {
                    bw.write(wifiBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            wifiBuf.clear();
        }

        public String SISTEENTO10(String strs[]) {
            long ans = 0;
            for (int i = 0; i < strs.length; i++) {
                for (int j = 0; j < strs[i].length(); j++) {
                    if (strs[i].charAt(j) >= 'a' && strs[i].charAt(j) <= 'f') {
                        ans = ans * 16 + 10 + (strs[i].charAt(j) - 'a');
                    } else {
                        ans = ans * 16 + (strs[i].charAt(j) - '0');
                    }
                }
            }
            return Long.toString(ans);
        }

        public void process(String strs[]) {
            StringBuilder sb = new StringBuilder();
            SensorTimeStamp = Math.min(SensorTimeStamp, Double.parseDouble(strs[2]));
            double time = Double.parseDouble(strs[2]) - SensorTimeStamp;
   
            sb.append(Double.toString(time) + "," + SISTEENTO10(strs[4].split(":")) + "," + strs[strs.length - 1]);
            wifiBuf.add(sb.toString());
            if (wifiBuf.size() == 100)
                writeFile();
            count++;
        }
    }

    public static class gnss {
        List<String> gnssBuf;
        double firsTime;
        String path;
        HashMap<Double, List<String>> map;

        gnss(String path, double time) {
            gnssBuf = new ArrayList<>();
            this.path = path;
            firsTime = time;
            map = new HashMap<>();
        }

        public void writeFile() {
            File file = new File(path);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < gnssBuf.size(); i++) {
                    bw.write(gnssBuf.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gnssBuf.clear();
        }

        public void process(String strs[]) {
            StringBuilder sb = new StringBuilder();
            StringBuilder mapStr = new StringBuilder();
            double Ctime = Double.parseDouble(strs[1]) - firsTime;
            double sts = Double.parseDouble(strs[2]);
            sb.append(Double.toString(Ctime) + "," + strs[3] + "," + strs[4] + "," + strs[5] + "," + strs[6] + ","
                    + strs[7] + "," + strs[8] + "," + strs[9] + "," + strs[10]);
            for (int i = 3; i < strs.length; i++) {
                mapStr.append(strs[i]);
            }
            List<String> list = map.getOrDefault(sts, new ArrayList<>());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(mapStr.toString()))
                    return;
            }
            gnssBuf.add(sb.toString());
            list.add(mapStr.toString());
            map.put(sts, list);
            if (gnssBuf.size() == 100)
                writeFile();
        }
    }

    public static void main(String[] args) {
        solution s = new solution();
        String inputPath = new String("D:\\陈鹏飞\\ATR\\logfile_2020_03_19_09_31_56.txt"),imuPath=new String("D:\\陈鹏飞\\ATR\\imu.txt");
        String wifiPath=new String("D:\\陈鹏飞\\ATR\\wifi.txt"),gnssPath = new String("D:\\陈鹏飞\\ATR\\gnss.txt");        
        imu IMU = new imu(imuPath);
        wifi WIFI = new wifi(wifiPath);
        gnss GNSS = new gnss(gnssPath, -1);

        File file = new File(inputPath);
        s.getMinSTS(file, WIFI, IMU, GNSS);
        s.ReadAndProcess(file, WIFI, IMU, GNSS);
    }

    public void ReadAndProcess(File file, wifi WIFI, imu IMU, gnss GNSS) {
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
               
                if (s.length() == 0 || s.charAt(0) == '%') {
                    continue;
                } 
                else {
                    String strs[] = s.split(";");
                    if (strs[0].equals("PRES"))
                        flag = true;
                    if (GNSS.firsTime == -1)
                        GNSS.firsTime = Double.parseDouble(strs[1]);
                    if (flag)
                        process(strs, s, WIFI, IMU, GNSS);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IMU.writeFile();
        WIFI.writeFile();
        GNSS.writeFile();
    }


    public void process(String[] strs, String s, wifi WIFI, imu IMU, gnss GNSS) {
        switch (strs[0]) {
        case "ACCE":
        case "GYRO":
        case "MAGN":
        case "PRES":
        case "AHRS":
            IMU.process(strs);
            break;
        case "WIFI":
            WIFI.process(strs);
            break;
        case "GNSS":
            GNSS.process(strs);
            break;
        }
    }

    public void getMinSTS(File file, wifi WIFI, imu IMU, gnss GNSS) {
        double minSts = Double.MAX_VALUE;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
               
                if (s.length() == 0 || s.charAt(0) == '%') {
                    continue;
                } 
                else {
                    String strs[] = s.split(";");
                    if (strs[0].equals("WIFI") || strs[0].equals("MAGN") || strs[0].equals("ACCE")
                            || strs[0].equals("GYRO") || strs[0].equals("PRES") || strs[0].equals("GNSS")
                            || strs[0].equals("AHRS")) {
                        minSts = Math.min(minSts, Double.parseDouble(strs[2]));
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WIFI.SensorTimeStamp = IMU.SensorTimeStamp = minSts;
    }

}