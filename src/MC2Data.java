import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MC2Data {	//�������ݴ���
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//��ʽ��ʱ����
	static int[] LocationNum =new int[5];										//��¼�ص㷢����Ϣ��
	static HashMap<String,Byte> hmLocation=null;								//����λ����
	static HashMap<Byte,String> hmLocationRev=null;								//����λ����
	static HashMap<Integer,Integer> hmIDfrom=null;								//��ʽ�����ͷ�ID
	static HashMap<Integer,Integer> hmIDfromRev=null;							//���鷢�ͷ�ID
	static HashMap<Integer,Integer> hmIDto=null;								//��ʽ�����շ�ID
	static HashMap<Integer,Integer> hmIDtoRev=null;								//������շ�ID
	static HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>> hm=null;		//�洢һ�������
	static HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>> hmNow=null;		//�洢ɸѡ�������
	static HashMap<Integer,Integer> hmIDfromNow=null;							//��ʽ��ɸѡ���ͷ�ID
	static HashMap<Integer,Integer> hmIDfromRevNow=null;						//����ɸѡ���ͷ�ID
	static HashMap<Integer,Integer> hmIDtoNow=null;								//��ʽ��ɸѡ����շ�ID
	static HashMap<Integer,Integer> hmIDtoRevNow=null;							//����ɸѡ����շ�ID
	Date date;	//����
	int from;	//���ͷ�ID
	int to;		//���շ�ID
	Byte loc;	//λ��
	public MC2Data(String Data){	//���캯������ʽ�����ݲ��洢
		String[]tmp=Data.split(",");	//�ָ��ַ���
		try{
			this.date=sdf.parse(tmp[0]);	//ת������
		}catch(Exception e){
			e.printStackTrace();
		}
		this.from=Integer.parseInt(tmp[1]);	//ת�����ͷ�ID
		if(tmp[2].compareTo("external")==0)this.to=-1;	//ת�����շ�ID (externel=-1)
		else this.to=Integer.parseInt(tmp[2]);
		
		Integer IDfrom=update_hmID(this.from,hmIDfrom,hmIDfromRev);	//��ʽ��ID
		Integer IDto=update_hmID(this.to,hmIDto,hmIDtoRev);			//��ʽ��ID
		HashMap<Integer, ArrayList<MC2Data>> ret;
		if((ret=hm.get(IDfrom))==null){	//���from�Ƿ��Ѵ��ڣ��������򴴽�
			ret=new HashMap<Integer, ArrayList<MC2Data>>();
			hm.put(IDfrom, ret);
		}
		ArrayList<MC2Data> arr=null;
		if((arr=ret.get(IDto))==null){
			arr=new ArrayList<MC2Data>();
			ret.put(IDto, arr);
		}
		arr.add(this);	//��to�������HashMap
		Byte loc;
		if((loc=hmLocation.get(tmp[3]))==null){	//λ�õĸ�ʽ����ӳ��ΪByte�����ٴ洢�ռ�
			loc=(byte)hmLocation.size();
			hmLocation.put(tmp[3], loc);
			hmLocationRev.put(loc, tmp[3]);
		}
		this.loc=loc;
		LocationNum[loc]++;	//���¸�λ����Ϣ��
	}
	
	//��ʽ��ID
	private static int update_hmID(int key,HashMap<Integer,Integer> hmID,HashMap<Integer,Integer> hmIDRev){
		Integer value;
		if((value=hmID.get(key))==null){	//�������ڣ��򸳸�����ǰHashMap�Ĵ�С���������Բ������㿪ʼ����������
			value=hmID.size();
			hmID.put(key,value);
			hmIDRev.put(value, key);	//�����ĳ�ʼ��
		}
		return value;
	}
	@Override
	public String toString() {	//��дtoString, �������
		return "Time:\t"+date
				+"\nFrom:\t"+from
				+"\nTo:\t"+to
				+"\nLocation:\t"+hmLocationRev.get(loc);
	}
	public static void setOverview(){	//�Ե���ʽ��ʾȫ������
		Constant.overview=true;
		hmNow=hm;
		hmIDfromNow=hmIDfrom;
		hmIDfromRevNow=hmIDfromRev;
		hmIDtoNow=hmIDto;
		hmIDtoRevNow=hmIDtoRev;
		Constant.panel.repaint();
		Constant.panel.setPreferredSize(new Dimension(MC2Data.hmIDfromNow.size()+Constant.size,
				MC2Data.hmIDfromNow.size()+Constant.size));
		Constant.scrollPanel.revalidate();
	}
	public static void filtByTime(String L, String R){	//��ʱ��ɸѡ
		Integer IDfrom=null;
		Integer IDto=null;
		Date dl=null;
		Date dr=null;
		try{	//��ʽ��ʱ��߽�
		dl=sdf.parse(L);
		dr=sdf.parse(R);
		}catch(Exception e){
			e.printStackTrace();
		}
		hmNow=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();	//��ʼ������
		hmIDfromNow=new HashMap<Integer,Integer>();
		hmIDfromRevNow=new HashMap<Integer,Integer>();
		if(Constant.samesubID){	//����ͼID����, ��from==to;
			hmIDtoNow=hmIDfromNow;
			hmIDtoRevNow=hmIDfromRevNow;
		}else{
			hmIDtoNow=new HashMap<Integer,Integer>();
			hmIDtoRevNow=new HashMap<Integer,Integer>();
		}
		for(int i=0;i<hmIDfrom.size();i++){	//����from
			HashMap<Integer, ArrayList<MC2Data>> ret=hm.get(i);
			if(ret!=null){	//�����ID����
				HashMap<Integer, ArrayList<MC2Data>> result=new HashMap<Integer,ArrayList<MC2Data>>();
				for(int j=0;j<hmIDto.size();j++){	//����to
					ArrayList<MC2Data> data=null;
					if((data=ret.get(j))!=null){
						ArrayList<MC2Data> newdata=new ArrayList<MC2Data>();
						for(int k=0;k<data.size();k++){
							MC2Data tmp=data.get(k);
							if(tmp.date.before(dr)&&tmp.date.after(dl)){	//�Ƚ����ڣ���������
								IDto=update_hmID(hmIDtoRev.get(j),hmIDtoNow,hmIDtoRevNow);
								newdata.add(tmp);
							}
						}
						if(newdata.size()>0)
							result.put(IDto, newdata);	
					}
				}
				if(result.size()>0){	//������˳�����������
					IDfrom=update_hmID(hmIDfromRev.get(i),hmIDfromNow,hmIDfromRevNow);
					hmNow.put(IDfrom, result);
				}
			}
		}
		Constant.overview=false;
		Constant.refreshDraw();
	}
	public static void filtByID(int XL, int XR, int YL, int YR){
		Constant.X=XL;
		Constant.Y=YL;
		Integer IDfrom=null;
		Integer IDto=null;
		hmNow=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();	//��ʼ������
		hmIDfromNow=new HashMap<Integer,Integer>();
		hmIDfromRevNow=new HashMap<Integer,Integer>();
		if(Constant.samesubID){	//����ͼID����, ��from==to;
			hmIDtoNow=hmIDfromNow;
			hmIDtoRevNow=hmIDfromRevNow;
		}else{
			hmIDtoNow=new HashMap<Integer,Integer>();
			hmIDtoRevNow=new HashMap<Integer,Integer>();
		}
		for(int i=0;i<hmIDfrom.size();i++){	//����from
			HashMap<Integer, ArrayList<MC2Data>> ret=hm.get(i);
			if(ret!=null){	//����ID����
				HashMap<Integer,ArrayList<MC2Data>> result=new HashMap<Integer,ArrayList<MC2Data>>();
				for(int j=0;j<hmIDto.size();j++){	//����to
					ArrayList<MC2Data> data=null;
					if((data=ret.get(j))!=null){
						ArrayList<MC2Data> newdata=new ArrayList<MC2Data>();
						for(int k=0;k<data.size();k++){
							MC2Data tmp=data.get(k);
							int x=hmIDfrom.get(tmp.from);
							int y=hmIDto.get(tmp.to);
							if(x>=XL&&x<=XR&&y>=YL&&y<=YR){	//����ID��Сɸѡ
								IDto=update_hmID(hmIDtoRev.get(j),hmIDtoNow,hmIDtoRevNow);
								newdata.add(tmp);
							}
						}
						if(newdata.size()>0)
							result.put(IDto, newdata);
					}
				}
				if(result.size()>0){	//��ɸѡ����������
					IDfrom=update_hmID(hmIDfromRev.get(i),hmIDfromNow,hmIDfromRevNow);
					hmNow.put(IDfrom, result);
				}
			}
		}
		Constant.overview=false;
		Constant.refreshDraw();
	}
	public static void exchangeX(int x1,int x2){	//����from ID
		Integer hmID1=hmIDfromNow.get(x1);	//ת������׼����
		Integer hmID2=hmIDfromNow.get(x2);
		if(hmID1==null||hmID2==null)return;	//��������Ч�򷵻�
		HashMap<Integer, ArrayList<MC2Data>> result1=hmNow.get(hmID1);	//��ȡ�洢��HashMap
		HashMap<Integer, ArrayList<MC2Data>> result2=hmNow.get(hmID2);
		hmNow.put(hmID1, result2);	//����ID��HashMap
		hmNow.put(hmID2, result1);
		hmIDfromNow.put(x1, hmID2);
		hmIDfromNow.put(x2, hmID1);
		hmIDfromRevNow.put(hmID1, x2);
		hmIDfromRevNow.put(hmID2, x1);
		Constant.panel.repaint();
	}
	public static void exchangeY(int y1,int y2){	//����to ID
		Integer hmID1=hmIDtoNow.get(y1);	//ת������׼����
		Integer hmID2=hmIDtoNow.get(y2);
		if(hmID1==null||hmID2==null)return;	//��������Ч�򷵻�
		for(int i=0;i<hmIDfromNow.size();i++){	//����from
			HashMap<Integer, ArrayList<MC2Data>> ret=hmNow.get(i);
			if(ret!=null){	//��ID��Ч
				ArrayList<MC2Data> result1=ret.get(hmID1);
				ArrayList<MC2Data> result2=ret.get(hmID2);
				ret.remove(hmID1);
				ret.remove(hmID2);
				if(result1!=null){	//������Ϊ���򲻴�
					ret.put(hmID2, result1);
				}
				if(result2!=null){
					ret.put(hmID1, result2);
				}
			}
		}
		hmIDtoNow.put(y1, hmID2);	//������׼ID
		hmIDtoNow.put(y2, hmID1);
		hmIDtoRevNow.put(hmID2, y1);
		hmIDtoRevNow.put(hmID1, y2);
		Constant.panel.repaint();
	}
}
