import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Constant {
	static JLabel xLabel=new JLabel("x: ID: ");	//显示X坐标和ID
	static JLabel yLabel=new JLabel("y: ID: "); //显示Y坐标和ID
	static JTextArea Info=new JTextArea();
	static String EmptyInfo="\n\n\n\n\n";
	static int size=15;		//字体大小
	static MainFrame wnd;	//主窗口
	static DrawPanel panel;	//画图界面
	static JScrollPane scrollPanel;	//绘制界面所在的滚动世界
	static ControlPanel ctrpanel;	//控制界面
	static int Day;	//哪一天
	static boolean overview;	//是否是预览(点阵显示/方格显示)
	static boolean ModeSelect;	//是否是鼠标选取区域模式
	static boolean sameID;		//横纵ID是否使用相同坐标
	static boolean samesubID;	//子图横纵ID是否使用相同坐标
	static MC2Data[] myData;		//存储读入数据
	static int X;	//当前坐标系起点
	static int Y;
	public static void setWnd(MainFrame wnd){	//记录窗口元素
		Constant.wnd=wnd;
		Constant.panel=wnd.panel;
		Constant.ctrpanel=wnd.ctrPanel;
		Constant.scrollPanel=wnd.scrollPanel;
	}
	public static void init(int choice){	//初始化，读入文件并处理
		X=0;	//当前坐标系起点设为零
		Y=0;
		MC2Data.hmLocation=new HashMap<String,Byte>();	//准备数据格式化相关变量
		MC2Data.hmLocationRev=new HashMap<Byte,String>();
		MC2Data.hmIDfrom=new HashMap<Integer,Integer>();
		MC2Data.hmIDfromRev=new HashMap<Integer,Integer>();
		for(int i=0;i<MC2Data.LocationNum.length;i++){
			MC2Data.LocationNum[i]=0;
		}
		if(sameID){	//若横纵ID关联, 则from==to;
			MC2Data.hmIDto=MC2Data.hmIDfrom;
			MC2Data.hmIDtoRev=MC2Data.hmIDfromRev;
		}else{
			MC2Data.hmIDto=new HashMap<Integer,Integer>();
			MC2Data.hmIDtoRev=new HashMap<Integer,Integer>();	
		}
		MC2Data.hm=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();
		String theDate=null;	//日期
		switch(choice){
		case 0:	//读周五的数据
			myData=new MC2Data[948739];
			theDate="Fri";
			break;
		case 1: //读周六的数据
			myData=new MC2Data[1655866];
			theDate="Sat";
			break;
		case 2: //读周日的数据
			myData=new MC2Data[1548724];
			theDate="Sun";
			break;
		}
		BufferedReader file=null;
		try{	//打开文件
			file=new BufferedReader(new FileReader("src/data/comm-data-"+theDate+".csv"));
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String tmp;
			tmp=file.readLine();	//读取第一行表头
			for(int i=0;i<myData.length;i++){
				tmp=file.readLine();	//读取一行
				myData[i]=new MC2Data(tmp);	//处理数据
			}
			file.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void refreshDraw(){	//刷新显示
		Constant.panel.repaint();
		Constant.panel.setPreferredSize(new Dimension(MC2Data.hmIDfromNow.size()*size+size,
				MC2Data.hmIDfromNow.size()*size+size));

		//Constant.scrollPanel.repaint();
		//Constant.scrollPanel.setPreferredSize(new Dimension(MC2Data.hmIDfromNow.size()*size+size,
		//		MC2Data.hmIDfromNow.size()*size+size));
		Constant.scrollPanel.revalidate();
	}
}
