import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Constant {
	static JLabel xLabel=new JLabel("x: ID: ");	//��ʾX�����ID
	static JLabel yLabel=new JLabel("y: ID: "); //��ʾY�����ID
	static JTextArea Info=new JTextArea();
	static String EmptyInfo="\n\n\n\n\n";
	static int size=15;		//�����С
	static MainFrame wnd;	//������
	static DrawPanel panel;	//��ͼ����
	static JScrollPane scrollPanel;	//���ƽ������ڵĹ�������
	static ControlPanel ctrpanel;	//���ƽ���
	static int Day;	//��һ��
	static boolean overview;	//�Ƿ���Ԥ��(������ʾ/������ʾ)
	static boolean ModeSelect;	//�Ƿ������ѡȡ����ģʽ
	static boolean sameID;		//����ID�Ƿ�ʹ����ͬ����
	static boolean samesubID;	//��ͼ����ID�Ƿ�ʹ����ͬ����
	static MC2Data[] myData;		//�洢��������
	static int X;	//��ǰ����ϵ���
	static int Y;
	public static void setWnd(MainFrame wnd){	//��¼����Ԫ��
		Constant.wnd=wnd;
		Constant.panel=wnd.panel;
		Constant.ctrpanel=wnd.ctrPanel;
		Constant.scrollPanel=wnd.scrollPanel;
	}
	public static void init(int choice){	//��ʼ���������ļ�������
		X=0;	//��ǰ����ϵ�����Ϊ��
		Y=0;
		MC2Data.hmLocation=new HashMap<String,Byte>();	//׼�����ݸ�ʽ����ر���
		MC2Data.hmLocationRev=new HashMap<Byte,String>();
		MC2Data.hmIDfrom=new HashMap<Integer,Integer>();
		MC2Data.hmIDfromRev=new HashMap<Integer,Integer>();
		for(int i=0;i<MC2Data.LocationNum.length;i++){
			MC2Data.LocationNum[i]=0;
		}
		if(sameID){	//������ID����, ��from==to;
			MC2Data.hmIDto=MC2Data.hmIDfrom;
			MC2Data.hmIDtoRev=MC2Data.hmIDfromRev;
		}else{
			MC2Data.hmIDto=new HashMap<Integer,Integer>();
			MC2Data.hmIDtoRev=new HashMap<Integer,Integer>();	
		}
		MC2Data.hm=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();
		String theDate=null;	//����
		switch(choice){
		case 0:	//�����������
			myData=new MC2Data[948739];
			theDate="Fri";
			break;
		case 1: //������������
			myData=new MC2Data[1655866];
			theDate="Sat";
			break;
		case 2: //�����յ�����
			myData=new MC2Data[1548724];
			theDate="Sun";
			break;
		}
		BufferedReader file=null;
		try{	//���ļ�
			file=new BufferedReader(new FileReader("src/data/comm-data-"+theDate+".csv"));
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String tmp;
			tmp=file.readLine();	//��ȡ��һ�б�ͷ
			for(int i=0;i<myData.length;i++){
				tmp=file.readLine();	//��ȡһ��
				myData[i]=new MC2Data(tmp);	//��������
			}
			file.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void refreshDraw(){	//ˢ����ʾ
		Constant.panel.repaint();
		Constant.panel.setPreferredSize(new Dimension(MC2Data.hmIDfromNow.size()*size+size,
				MC2Data.hmIDfromNow.size()*size+size));

		//Constant.scrollPanel.repaint();
		//Constant.scrollPanel.setPreferredSize(new Dimension(MC2Data.hmIDfromNow.size()*size+size,
		//		MC2Data.hmIDfromNow.size()*size+size));
		Constant.scrollPanel.revalidate();
	}
}
