import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class DrawPanel extends JPanel{	//��ͼ����
	int x, y, x1, y1, x2, y2, x3, y3;	//�������
	boolean two;	//�Ƿ����ڶ���
	static Color[] locColor=new Color[]{
								Color.RED,
								Color.BLUE,
								Color.CYAN,
								Color.GREEN,
								Color.ORANGE
							};
	public DrawPanel() {	//���캯��
		this.addMouseMotionListener(new MouseMotionListener() {	//����϶����ƶ�����Ӧ
			@Override
			public void mouseDragged(MouseEvent e) {	//ѡȡʱ���ư�͸������
				x=e.getX();
				y=e.getY();
				repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {	//�����ͣ��ʾ��Ϣ
				x3=e.getX()-Constant.size;	//��ȡ��ǰ����
				y3=e.getY()-Constant.size;
				if(Constant.overview){
					if(x3>=0&&y3>0){	//ID��Ч
						String text="";
						HashMap<Integer, ArrayList<MC2Data>> ret=null;
						if((ret=MC2Data.hm.get(x3))!=null){
							ArrayList<MC2Data> data=ret.get(y3);
							if(data!=null){
								text=data.get(0).toString()+
										"\nTimes:\t"+data.size();
								setToolTipText(text);	//��ʾ��
								Constant.Info.setText(text);	//���ƽ�����ʾ
							}else{
								Constant.Info.setText(Constant.EmptyInfo);
							}
						}
					}else{
						Constant.Info.setText(Constant.EmptyInfo);
					}
					Constant.xLabel.setText("x: "+x3+" ID: "+MC2Data.hmIDfromRev.get(x3));	//�����ID
					Constant.yLabel.setText("y: "+y3+" ID: "+MC2Data.hmIDtoRev.get(y3));
				}
				else if(MC2Data.hmNow!=null){	//��ͼ
					x3=x3/Constant.size;	//����ת��
					y3=y3/Constant.size;
					if(x3>=0&&x3<MC2Data.hmIDfromNow.size()){
						Constant.xLabel.setText("x: "+x3+" ID: "+MC2Data.hmIDfromRevNow.get(x3));	//�����ID
						Constant.yLabel.setText("y: "+y3+" ID: "+MC2Data.hmIDtoRevNow.get(y3));
						HashMap<Integer, ArrayList<MC2Data>> ret=null;
						if((ret=MC2Data.hmNow.get(x3))!=null){
							ArrayList<MC2Data> data=ret.get(y3);
							if(data==null){
								Constant.Info.setText(Constant.EmptyInfo);
							}else{
								String text=data.get(0).toString()+
										"\nTimes:\t"+data.size();
								Constant.Info.setText(text);	//���ƽ�����ʾ
								setToolTipText(text);	//��ʾ��
							}
						}else{
							Constant.Info.setText(Constant.EmptyInfo);
						}
					}
				}
			}
		});
		this.addMouseListener(new MouseAdapter(){	//�����갴�����ɿ�����Ӧ
			@Override
			public void mouseReleased(MouseEvent e) {
				if(Constant.ModeSelect==true){	//ѡȡģʽ�»�ȡ���½ǵ�����꣬������������
					x2=e.getX()-Constant.size;
					y2=e.getY()-Constant.size;
					if(Constant.overview==false){	//��ΪС���Σ���ת������
						x1=(x1/Constant.size)-1+Constant.X;
						x2=(x2/Constant.size)+Constant.X;
						y1=(y1/Constant.size)-1+Constant.X;
						y2=(y2/Constant.size)+Constant.X;
					}
					MC2Data.filtByID(x1,x2,y1,y2);	//����ɸѡ
					Constant.ctrpanel.btnMouseSelect.setEnabled(true);
					Constant.ctrpanel.IDXL.setText(x1+"");	//������ʾ������
					Constant.ctrpanel.IDYL.setText(y1+"");
					Constant.ctrpanel.IDXR.setText(x2+"");
					Constant.ctrpanel.IDYR.setText(x2+"");
					Constant.ModeSelect=false;
					repaint();
					two=false;	//���õ������
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {	//��ȡѡȡ���Ͻǵ�����
				if(MC2Data.hmNow!=null){

					int x,y;
					x1=e.getX();
					y1=e.getY();
					x=x1-Constant.size;
					y=y1-Constant.size;
					if(Constant.overview==false){	//����ת��
						x=x/Constant.size;
						y=y/Constant.size;
					}
					if(x>=0&&x<MC2Data.hmIDfromNow.size()&&y>=0&&y<MC2Data.hmIDtoNow.size()){
						if(two==false){	//��һ����
							Constant.ctrpanel.IDXL.setText(MC2Data.hmIDfromRevNow.get(x).toString());
							Constant.ctrpanel.IDYL.setText(MC2Data.hmIDtoRevNow.get(y).toString());
							two=true;
						}else{	//�ڶ�����
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
	protected void paintComponent(Graphics g) {	//����
		super.paintComponent(g);
		if(Constant.ModeSelect){	//����ѡȡ����
			g.setColor(new Color(0,255,255,50));
			g.fillRect(x1, y1, x-x1, y-y1);
		}
		g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));
		g.setColor(Color.BLACK);	//�������������
		g.drawString("from", Constant.size, Constant.size-2);
		g.drawString("to", 0, 300);
		if(MC2Data.hmLocation!=null){	//��������ͼ��
			int offset=Constant.size*5;
			for(Byte i=0;i<MC2Data.hmLocation.size();i++){
				g.setColor(locColor[i]);
				g.fillRect(i*Constant.size+offset+1, 1, Constant.size-2, Constant.size-2);
				g.setColor(Color.BLACK);
				g.drawString(MC2Data.hmLocationRev.get(i), i*Constant.size+Constant.size+offset, Constant.size-2);
				offset+=Constant.size*2+Constant.size*MC2Data.hmLocationRev.get(i).length()/2;
			}
		}
		if(Constant.overview){	//������ʾȫ��
			for(int i=0;i<MC2Data.hmIDfrom.size();i++){	//����from
				HashMap<Integer, ArrayList<MC2Data>> ret;
				if((ret=MC2Data.hm.get(i))!=null){	//��ID��Ч
					for(int j=0;j<MC2Data.hmIDfrom.size();j++){	//����to
						ArrayList<MC2Data> cur=ret.get(j);
						if(cur!=null){
							g.setColor(locColor[cur.get(0).loc]);	//���ݵص�������ɫ
							g.drawLine(MC2Data.hmIDfrom.get(cur.get(0).from)+Constant.size, MC2Data.hmIDto.get(cur.get(0).to)+Constant.size,
									MC2Data.hmIDfrom.get(cur.get(0).from)+Constant.size, MC2Data.hmIDto.get(cur.get(0).to)+Constant.size);
						}
						
					}
				}
			}
		}else if(MC2Data.hmIDfromNow!=null){	//��ʾ����
			for(int i=0;i<MC2Data.hmIDfromNow.size();i++){	//����from
				HashMap<Integer, ArrayList<MC2Data>> ret;
				if((ret=MC2Data.hmNow.get(i))!=null){	//��ID��Ч
					for(int j=0;j<MC2Data.hmIDtoNow.size();j++){	//����to
						ArrayList<MC2Data> cur=ret.get(j);
						if(cur!=null){	//������
							g.setColor(locColor[cur.get(0).loc]);	//���ݷ��ŵص�ѡ����ɫ
							g.fillRect(MC2Data.hmIDfromNow.get(cur.get(0).from)*Constant.size+Constant.size+1, 
									MC2Data.hmIDtoNow.get(cur.get(0).to)*Constant.size+Constant.size+1,
									Constant.size-2, Constant.size-2);
							g.setFont(new Font("Courier New",Font.PLAIN,12));
							g.setColor(Color.BLACK);
							g.drawString(cur.size()+"",		//��ʾ��Ϣ����
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
