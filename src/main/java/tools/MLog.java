package tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MLog {
    public static String split = ",";

    public static void i(String logt, Object... objects) {
        System.err.print(logt + ":");
        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i]);
        }
        System.err.println();
//		try {
//			FileOutputStream fos = new FileOutputStream(new File(SQLSetting.LOG_PATH+"log_"+GetNowDate()+".log"), true);
//			System.err.print(logt+":");
//			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
//			osw.append(GetTime()+logt+":\r\n");
//
//			for(int i = 0; i < objects.length;i++)
//			{
//				System.out.print(objects[i]);
//				osw.append(objects[i]+"");
//				if(i<objects.length-1)
//				{
//					System.err.println(split);
//					osw.append(split+"\r\n");
//				}
//			}
//			System.err.println();
//			osw.append("\r\n");
//
//			osw.flush();
//			osw.close();
//
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    }

    public static void e(String logt, Object... objects) {
        System.err.print(logt + ":");
        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i]);
        }
        System.err.println();
//		try {
//			FileOutputStream fos = new FileOutputStream(new File(SQLSetting.LOG_PATH+"logerr_"+GetNowDate()+".log"), true);
//			System.err.print(logt+":");
//			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
//			osw.append(GetTime()+logt+":");
//
//			for(int i = 0; i < objects.length;i++)
//			{
//				System.out.print(objects[i]);
//				osw.append(objects[i]+"");
//				if(i<objects.length-1)
//				{
//					System.err.print(split);
//					osw.append(split);
//				}
//			}
//			System.err.println();
//			osw.append("\r\n");
//
//			osw.flush();
//			osw.close();
//
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    }

    public static void e(String logt, String msg, StackTraceElement[] objects) {
        System.err.print(logt + ":");
        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i]);
        }
        System.err.println();
//		try {
//			FileOutputStream fos = new FileOutputStream(new File(SQLSetting.LOG_PATH+"logerr_"+GetNowDate()+".log"), true);
//			System.err.print(logt+":");
//			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
//			osw.append(GetTime()+logt+":"+msg);
//
//			for(int i = 0; i < objects.length;i++)
//			{
//				System.out.print(objects[i]);
//				osw.append(objects[i]+"");
//				if(i<objects.length-1)
//				{
//					System.err.println(split);
//					osw.append(split+"\r\n");
//				}
//			}
//			System.err.println();
//			osw.append("\r\n");
//
//			osw.flush();
//			osw.close();
//
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    }


    private static String GetNowDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String format = simpleDateFormat.format(c.getTime());
        return format;
    }

    private static String GetTime() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_mm_ss");
        String format = simpleDateFormat.format(c.getTime());
        return format;
    }
}

