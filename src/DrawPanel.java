import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class DrawPanel extends JPanel{	//画图界面
	int x, y, x1, y1, x2, y2, x3, y3;	//鼠标坐标
	boolean two;	//是否点击第二次
	static Color[] locColor=new Color[]{
								Color.RED,
								Color.BLUE,
								Color.CYAN,
								Color.GREEN,
								Color.ORANGE
							};
	public DrawPanel() {	//构造函数
		this.addMouseMotionListener(new MouseMotionListener() {	//添加拖动和移动的响应
			@Override
			public void mouseDragged(MouseEvent e) {	//选取时绘制半透明矩形
				x=e.getX();
				y=e.getY();
				repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {	//鼠标悬停显示信息
				x3=e.getX()-Constant.size;	//获取当前坐标
				y3=e.getY()-Constant.size;
				if(Constant.overview){
					if(x3>=0&&y3>0){	//ID有效
						String text="";
						HashMap<Integer, ArrayList<MC2Data>> ret=null;
						if((ret=MC2Data.hm.get(x3))!=null){
							ArrayList<MC2Data> data=ret.get(y3);
							if(data!=null){
								text=data.get(0).toString()+
										"\nTimes:\t"+data.size();
								setToolTipText(text);	//提示条
								Constant.Info.setText(text);	//控制界面显示
							}else{
								Constant.Info.setText(Constant.EmptyInfo);
							}
						}
					}else{
						Constant.Info.setText(Constant.EmptyInfo);
					}
					Constant.xLabel.setText("x: "+x3+" ID: "+MC2Data.hmIDfromRev.get(x3));	//坐标和ID
					Constant.yLabel.setText("y: "+y3+" ID: "+MC2Data.hmIDtoRev.get(y3));
				}
				else if(MC2Data.hmNow!=null){	//子图
					x3=x3/Constant.size;	//坐标转换
					y3=y3/Constant.size;
					if(x3>=0&&x3<MC2Data.hmIDfromNow.size()){
						Constant.xLabel.setText("x: "+x3+" ID: "+MC2Data.hmIDfromRevNow.get(x3));	//坐标和ID
						Constant.yLabel.setText("y: "+y3+" ID: "+MC2Data.hmIDtoRevNow.get(y3));
						HashMap<Integer, ArrayList<MC2Data>> ret=null;
						if((ret=MC2Data.hmNow.get(x3))!=null){
							ArrayList<MC2Data> data=ret.get(y3);
							if(data==null){
								Constant.Info.setText(Constant.EmptyInfo);
							}else{
								String text=data.get(0).toString()+
										"\nTimes:\t"+data.size();
								Constant.Info.setText(text);	//控制界面显示
								setToolTipText(text);	//提示条
							}
						}else{
							Constant.Info.setText(Constant.EmptyInfo);
						}
					}
				}
			}
		});
		this.addMouseListener(new MouseAdapter(){	//添加鼠标按下与松开的响应
			@Override
			public void mouseReleased(MouseEvent e) {
				if(Constant.ModeSelect==true){	//选取模式下获取右下角点的坐标，并填入控制面板
					x2=e.getX()-Constant.size;
					y2=e.getY()-Constant.size;
					if(Constant.overview==false){	//若为小矩形，则转换坐标
						x1=(x1/Constant.size)-1+Constant.X;
						x2=(x2/Constant.size)+Constant.X;
						y1=(y1/Constant.size)-1+Constant.X;
						y2=(y2/Constant.size)+Constant.X;
					}
					MC2Data.filtByID(x1,x2,y1,y2);	//调用筛选
					Constant.ctrpanel.btnMouseSelect.setEnabled(true);
					Constant.ctrpanel.IDXL.setText(x1+"");	//更改显示的坐标
					Constant.ctrpanel.IDYL.setText(y1+"");
					Constant.ctrpanel.IDXR.setText(x2+"");
					Constant.ctrpanel.IDYR.setText(x2+"");
					Constant.ModeSelect=false;
					repaint();
					two=false;	//重置点击次数
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {	//获取选取左上角的坐标
				if(MC2Data.hmNow!=null){

					int x,y;
					x1=e.getX();
					y1=e.getY();
					x=x1-Constant.size;
					y=y1-Constant.size;
					if(Constant.overview==false){	//坐标转换
						x=x/Constant.size;
						y=y/Constant.size;
					}
					if(x>=0&&x<MC2Data.hmIDfromNow.size()&&y>=0&&y<MC2Data.hmIDtoNow.size()){
						if(two==false){	//第一个点
							Constant.ctrpanel.IDXL.setText(MC2Data.hmIDfromRevNow.get(x).toString());
							Constant.ctrpanel.IDYL.setText(MC2Data.hmIDtoRevNow.get(y).toString());
							two=true;
						}else{	//第二个点
							Constant.ctrpanel.IDXR.setText(MC2Data.hmIDfromRevNow.get(x).toString());
							Constant.ctrpanel.IDYR.setText(MC2Data.hmIDtoRevNow.get(y).toString());
							two=false;
						}
					}
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	@Override
	protected void paintComponent(Graphics g) {	//绘制
		super.paintComponent(g);
		if(Constant.ModeSelect){	//绘制选取矩形
			g.setColor(new Color(0,255,255,50));
			g.fillRect(x1, y1, x-x1, y-y1);
		}
		g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));
		g.setColor(Color.BLACK);	//绘制坐标的名字
		g.drawString("from", Constant.size, Constant.size-2);
		g.drawString("to", 0, 300);
		if(MC2Data.hmLocation!=null){	//绘制区域图例
			int offset=Constant.size*5;
			for(Byte i=0;i<MC2Data.hmLocation.size();i++){
				g.setColor(locColor[i]);
				g.fillRect(i*Constant.size+offset+1, 1, Constant.size-2, Constant.size-2);
				g.setColor(Color.BLACK);
				g.drawString(MC2Data.hmLocationRev.get(i), i*Constant.size+Constant.size+offset, Constant.size-2);
				offset+=Constant.size*2+Constant.size*MC2Data.hmLocationRev.get(i).length()/2;
			}
		}
		if(Constant.overview){	//点阵显示全部
			for(int i=0;i<MC2Data.hmIDfrom.size();i++){	//遍历from
				HashMap<Integer, ArrayList<MC2Data>> ret;
				if((ret=MC2Data.hm.get(i))!=null){	//若ID有效
					for(int j=0;j<MC2Data.hmIDfrom.size();j++){	//遍历to
						ArrayList<MC2Data> cur=ret.get(j);
						if(cur!=null){
							g.setColor(locColor[cur.get(0).loc]);	//根据地点设置颜色
							g.drawLine(MC2Data.hmIDfrom.get(cur.get(0).from)+Constant.size, MC2Data.hmIDto.get(cur.get(0).to)+Constant.size,
									MC2Data.hmIDfrom.get(cur.get(0).from)+Constant.size, MC2Data.hmIDto.get(cur.get(0).to)+Constant.size);
						}
						
					}
				}
			}
		}else if(MC2Data.hmIDfromNow!=null){	//显示矩阵
			for(int i=0;i<MC2Data.hmIDfromNow.size();i++){	//遍历from
				HashMap<Integer, ArrayList<MC2Data>> ret;
				if((ret=MC2Data.hmNow.get(i))!=null){	//若ID有效
					for(int j=0;j<MC2Data.hmIDtoNow.size();j++){	//遍历to
						ArrayList<MC2Data> cur=ret.get(j);
						if(cur!=null){	//画矩形
							g.setColor(locColor[cur.get(0).loc]);	//根据发信地点选择颜色
							g.fillRect(MC2Data.hmIDfromNow.get(cur.get(0).from)*Constant.size+Constant.size+1, 
									MC2Data.hmIDtoNow.get(cur.get(0).to)*Constant.size+Constant.size+1,
									Constant.size-2, Constant.size-2);
							g.setFont(new Font("Courier New",Font.PLAIN,12));
							g.setColor(Color.BLACK);
							g.drawString(cur.size()+"",		//显示消息数量
									MC2Data.hmIDfromNow.get(cur.get(0).from)*Constant.size+Constant.size+(cur.size()<10?Constant.size/2:0), 
									MC2Data.hmIDtoNow.get(cur.get(0).to)*Constant.size+Constant.size*2-2);
						}
						
					}
				}
				
			}
		}
		g.dispose();
	}
}
