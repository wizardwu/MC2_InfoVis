import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MC2Data {	//核心数据处理
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//格式化时间用
	static int[] LocationNum =new int[5];										//记录地点发送信息量
	static HashMap<String,Byte> hmLocation=null;								//反查位置用
	static HashMap<Byte,String> hmLocationRev=null;								//反查位置用
	static HashMap<Integer,Integer> hmIDfrom=null;								//格式化发送方ID
	static HashMap<Integer,Integer> hmIDfromRev=null;							//反查发送方ID
	static HashMap<Integer,Integer> hmIDto=null;								//格式化接收方ID
	static HashMap<Integer,Integer> hmIDtoRev=null;								//反查接收方ID
	static HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>> hm=null;		//存储一天的数据
	static HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>> hmNow=null;		//存储筛选后的数据
	static HashMap<Integer,Integer> hmIDfromNow=null;							//格式化筛选后发送方ID
	static HashMap<Integer,Integer> hmIDfromRevNow=null;						//反查筛选后发送方ID
	static HashMap<Integer,Integer> hmIDtoNow=null;								//格式化筛选后接收方ID
	static HashMap<Integer,Integer> hmIDtoRevNow=null;							//反查筛选后接收方ID
	Date date;	//日期
	int from;	//发送方ID
	int to;		//接收方ID
	Byte loc;	//位置
	public MC2Data(String Data){	//构造函数，格式化数据并存储
		String[]tmp=Data.split(",");	//分割字符串
		try{
			this.date=sdf.parse(tmp[0]);	//转换日期
		}catch(Exception e){
			e.printStackTrace();
		}
		this.from=Integer.parseInt(tmp[1]);	//转换发送发ID
		if(tmp[2].compareTo("external")==0)this.to=-1;	//转换接收方ID (externel=-1)
		else this.to=Integer.parseInt(tmp[2]);
		
		Integer IDfrom=update_hmID(this.from,hmIDfrom,hmIDfromRev);	//格式化ID
		Integer IDto=update_hmID(this.to,hmIDto,hmIDtoRev);			//格式化ID
		HashMap<Integer, ArrayList<MC2Data>> ret;
		if((ret=hm.get(IDfrom))==null){	//检查from是否已存在，不存在则创建
			ret=new HashMap<Integer, ArrayList<MC2Data>>();
			hm.put(IDfrom, ret);
		}
		ArrayList<MC2Data> arr=null;
		if((arr=ret.get(IDto))==null){
			arr=new ArrayList<MC2Data>();
			ret.put(IDto, arr);
		}
		arr.add(this);	//按to存入二级HashMap
		Byte loc;
		if((loc=hmLocation.get(tmp[3]))==null){	//位置的格式化，映射为Byte，减少存储空间
			loc=(byte)hmLocation.size();
			hmLocation.put(tmp[3], loc);
			hmLocationRev.put(loc, tmp[3]);
		}
		this.loc=loc;
		LocationNum[loc]++;	//更新各位置信息量
	}
	
	//格式化ID
	private static int update_hmID(int key,HashMap<Integer,Integer> hmID,HashMap<Integer,Integer> hmIDRev){
		Integer value;
		if((value=hmID.get(key))==null){	//若不存在，则赋给它当前HashMap的大小，这样可以产生从零开始的连续序列
			value=hmID.size();
			hmID.put(key,value);
			hmIDRev.put(value, key);	//反查表的初始化
		}
		return value;
	}
	@Override
	public String toString() {	//重写toString, 便于输出
		return "Time:\t"+date
				+"\nFrom:\t"+from
				+"\nTo:\t"+to
				+"\nLocation:\t"+hmLocationRev.get(loc);
	}
	public static void setOverview(){	//以点阵方式显示全部数据
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
	public static void filtByTime(String L, String R){	//按时间筛选
		Integer IDfrom=null;
		Integer IDto=null;
		Date dl=null;
		Date dr=null;
		try{	//格式化时间边界
		dl=sdf.parse(L);
		dr=sdf.parse(R);
		}catch(Exception e){
			e.printStackTrace();
		}
		hmNow=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();	//初始化变量
		hmIDfromNow=new HashMap<Integer,Integer>();
		hmIDfromRevNow=new HashMap<Integer,Integer>();
		if(Constant.samesubID){	//若子图ID关联, 则from==to;
			hmIDtoNow=hmIDfromNow;
			hmIDtoRevNow=hmIDfromRevNow;
		}else{
			hmIDtoNow=new HashMap<Integer,Integer>();
			hmIDtoRevNow=new HashMap<Integer,Integer>();
		}
		for(int i=0;i<hmIDfrom.size();i++){	//遍历from
			HashMap<Integer, ArrayList<MC2Data>> ret=hm.get(i);
			if(ret!=null){	//如果该ID存在
				HashMap<Integer, ArrayList<MC2Data>> result=new HashMap<Integer,ArrayList<MC2Data>>();
				for(int j=0;j<hmIDto.size();j++){	//遍历to
					ArrayList<MC2Data> data=null;
					if((data=ret.get(j))!=null){
						ArrayList<MC2Data> newdata=new ArrayList<MC2Data>();
						for(int k=0;k<data.size();k++){
							MC2Data tmp=data.get(k);
							if(tmp.date.before(dr)&&tmp.date.after(dl)){	//比较日期，更新数据
								IDto=update_hmID(hmIDtoRev.get(j),hmIDtoNow,hmIDtoRevNow);
								newdata.add(tmp);
							}
						}
						if(newdata.size()>0)
							result.put(IDto, newdata);	
					}
				}
				if(result.size()>0){	//如果过滤出结果，则存入
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
		hmNow=new HashMap<Integer,HashMap<Integer,ArrayList<MC2Data>>>();	//初始化变量
		hmIDfromNow=new HashMap<Integer,Integer>();
		hmIDfromRevNow=new HashMap<Integer,Integer>();
		if(Constant.samesubID){	//若子图ID关联, 则from==to;
			hmIDtoNow=hmIDfromNow;
			hmIDtoRevNow=hmIDfromRevNow;
		}else{
			hmIDtoNow=new HashMap<Integer,Integer>();
			hmIDtoRevNow=new HashMap<Integer,Integer>();
		}
		for(int i=0;i<hmIDfrom.size();i++){	//遍历from
			HashMap<Integer, ArrayList<MC2Data>> ret=hm.get(i);
			if(ret!=null){	//若该ID存在
				HashMap<Integer,ArrayList<MC2Data>> result=new HashMap<Integer,ArrayList<MC2Data>>();
				for(int j=0;j<hmIDto.size();j++){	//遍历to
					ArrayList<MC2Data> data=null;
					if((data=ret.get(j))!=null){
						ArrayList<MC2Data> newdata=new ArrayList<MC2Data>();
						for(int k=0;k<data.size();k++){
							MC2Data tmp=data.get(k);
							int x=hmIDfrom.get(tmp.from);
							int y=hmIDto.get(tmp.to);
							if(x>=XL&&x<=XR&&y>=YL&&y<=YR){	//根据ID大小筛选
								IDto=update_hmID(hmIDtoRev.get(j),hmIDtoNow,hmIDtoRevNow);
								newdata.add(tmp);
							}
						}
						if(newdata.size()>0)
							result.put(IDto, newdata);
					}
				}
				if(result.size()>0){	//若筛选出结果则存入
					IDfrom=update_hmID(hmIDfromRev.get(i),hmIDfromNow,hmIDfromRevNow);
					hmNow.put(IDfrom, result);
				}
			}
		}
		Constant.overview=false;
		Constant.refreshDraw();
	}
	public static void exchangeX(int x1,int x2){	//交换from ID
		Integer hmID1=hmIDfromNow.get(x1);	//转换到标准坐标
		Integer hmID2=hmIDfromNow.get(x2);
		if(hmID1==null||hmID2==null)return;	//若坐标无效则返回
		HashMap<Integer, ArrayList<MC2Data>> result1=hmNow.get(hmID1);	//获取存储的HashMap
		HashMap<Integer, ArrayList<MC2Data>> result2=hmNow.get(hmID2);
		hmNow.put(hmID1, result2);	//交换ID和HashMap
		hmNow.put(hmID2, result1);
		hmIDfromNow.put(x1, hmID2);
		hmIDfromNow.put(x2, hmID1);
		hmIDfromRevNow.put(hmID1, x2);
		hmIDfromRevNow.put(hmID2, x1);
		Constant.panel.repaint();
	}
	public static void exchangeY(int y1,int y2){	//交换to ID
		Integer hmID1=hmIDtoNow.get(y1);	//转换到标准坐标
		Integer hmID2=hmIDtoNow.get(y2);
		if(hmID1==null||hmID2==null)return;	//若坐标无效则返回
		for(int i=0;i<hmIDfromNow.size();i++){	//遍历from
			HashMap<Integer, ArrayList<MC2Data>> ret=hmNow.get(i);
			if(ret!=null){	//若ID有效
				ArrayList<MC2Data> result1=ret.get(hmID1);
				ArrayList<MC2Data> result2=ret.get(hmID2);
				ret.remove(hmID1);
				ret.remove(hmID2);
				if(result1!=null){	//交换，为空则不存
					ret.put(hmID2, result1);
				}
				if(result2!=null){
					ret.put(hmID1, result2);
				}
			}
		}
		hmIDtoNow.put(y1, hmID2);	//交换标准ID
		hmIDtoNow.put(y2, hmID1);
		hmIDtoRevNow.put(hmID2, y1);
		hmIDtoRevNow.put(hmID1, y2);
		Constant.panel.repaint();
	}
}
