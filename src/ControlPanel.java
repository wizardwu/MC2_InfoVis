import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ControlPanel extends JPanel{	//控制面板
	JButton btnMouseSelect;	//选取模式选择
	JTextField IDXL;	//xl
	JTextField IDXR;	//xr
	JTextField IDYL;	//yl
	JTextField IDYR;	//yr
	public ControlPanel(){
		setLayout(new GridBagLayout());	//设置布局
		GridBagConstraints gbc;        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(2, 1, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.LINE_START;
        
        ButtonGroup btnGroup=new ButtonGroup();	//使单选框成为一组
        //JLabel dayLabel=new JLabel("选择日期与模式");
        JCheckBox sameID=new JCheckBox("横纵ID关联");
        JCheckBox samesubID=new JCheckBox("子图关联");
		JRadioButton Friday=new JRadioButton("Friday");
		JRadioButton Saturday=new JRadioButton("Saturday");
		JRadioButton Sunday=new JRadioButton("Sunday");
        btnGroup.add(Friday);	//使单选框成为一组
        btnGroup.add(Saturday);
        btnGroup.add(Sunday);
        //add(dayLabel);
        add(sameID);
        gbc.gridy = GridBagConstraints.RELATIVE;
        add(samesubID);
        add(Friday, gbc);
        add(Saturday, gbc);
        add(Sunday, gbc);
		JButton btnStart=new JButton("加载");
		add(btnStart,gbc);
		JComboBox<String> combo=new JComboBox<String>(new String[]{"地点分布饼图","一对ID消息"});
		JButton showChart=new JButton("显示");
		add(combo);
		add(showChart);
		JLabel timeInfo=new JLabel("时间:(hh:mm:ss)");
		JTextField timeL=new JTextField();
		JTextField timeR=new JTextField();
		JButton timebtn=new JButton("按时间筛选");
		add(timeInfo,gbc);
		add(timeL,gbc);
		add(timeR,gbc);
		add(timebtn,gbc);
		
		JLabel rangeInfo=new JLabel("hmID范围");
		btnMouseSelect=new JButton("鼠标选取");
		IDXL=new JTextField("XL");
		IDXR=new JTextField("XR");
		IDYL=new JTextField("YL");
		IDYR=new JTextField("YR");
		JButton IDbtn=new JButton("按hmID筛选");
		add(rangeInfo);
		add(btnMouseSelect);
		add(IDXL,gbc);
		add(IDXR,gbc);
		add(IDYL,gbc);
		add(IDYR,gbc);
		add(IDbtn,gbc);

		
		JButton IDXexchange=new JButton("交换XID");
		JButton IDYexchange=new JButton("交换YID");
		add(IDXexchange);
		add(IDYexchange);
		
		
		add(Constant.xLabel,gbc);
		add(Constant.yLabel,gbc);
		Constant.Info.setEditable(false);
		Constant.Info.setLineWrap(true);
		Constant.Info.setWrapStyleWord(true);
		Constant.Info.setText(Constant.EmptyInfo);
		add(Constant.Info,gbc);
		
		btnStart.addActionListener(new ActionListener() {	//响应加载按钮
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sameID.isSelected()){	//是否横纵ID关联
					Constant.sameID=true;
				}else{
					Constant.sameID=false;
				}
				if(samesubID.isSelected()){	//子图是否横纵ID关联
					Constant.samesubID=true;
				}else{
					Constant.samesubID=false;
				}
				if(Friday.isSelected()){	//日期选择
					Constant.init(0);
				}
				if(Saturday.isSelected()){
					Constant.init(1);
				}
				if(Sunday.isSelected()){
					Constant.init(2);
				}
				MC2Data.setOverview();	//显示总体点阵视图
			}
		});
		btnMouseSelect.addActionListener(new ActionListener() {	//响应鼠标选取按钮
			@Override
			public void actionPerformed(ActionEvent e) {
				btnMouseSelect.setEnabled(false);	//禁用按钮
				Constant.ModeSelect=true;			//进入选取模式
			}
		});
		timebtn.addActionListener(new ActionListener() {	//响应时间筛选
			@Override
			public void actionPerformed(ActionEvent e) {
				String time="";
				if(Friday.isSelected()){
					time="2014-6-06 ";
				}
				if(Saturday.isSelected()){
					time="2014-6-07 ";
				}
				if(Sunday.isSelected()){
					time="2014-6-08 ";
				}
				MC2Data.filtByTime(time+timeL.getText(),
				time+timeR.getText());
			}
		});
		
		IDbtn.addActionListener(new ActionListener() {	//响应ID筛选
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.filtByID(Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()),
						Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()));
			}
		});
		IDXexchange.addActionListener(new ActionListener() {	//X交换
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.exchangeX(Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()));
			}
		});
		IDYexchange.addActionListener(new ActionListener() {	//Y交换
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.exchangeY(Integer.parseInt(IDYL.getText()),Integer.parseInt(IDYR.getText()));
			}
		});
		
		showChart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0){	//画消息分布饼图
					JFrame frame = new JFrame("消息发送地点分布");
					frame.setBounds(100,100,600,300);
					JPanel panel=new JPanel(){
						public void paintComponent(Graphics g) {
							String text="消息发送地点分布";		//标题
							g.setFont(new Font("隶书",Font.PLAIN,20));
							g.drawString(text, 200-text.length()*20/2, 40);
							double sum=0;
							double[] degree=new double[MC2Data.LocationNum.length+1];
							for(int i=0;i<MC2Data.LocationNum.length;i++){	//求和
								sum+=MC2Data.LocationNum[i];
							}
							degree[0]=0;
							for(Byte i=0;i<MC2Data.LocationNum.length;i++){	//求度数并绘制饼图
								degree[i+1]=MC2Data.LocationNum[i]*360/sum+degree[i];	//相对于起始边的度数
								g.setColor(DrawPanel.locColor[i]);	//设置颜色
								g.fillArc(100, 50, 200, 200, (int)degree[i], (int)(degree[i+1]-degree[i])+1);	//画饼图
								g.fillRect(400-Constant.size*3/2, 50+3*i*Constant.size-Constant.size, Constant.size, Constant.size);//画图例
								g.setColor(Color.BLACK);
								g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));	//文字
								g.drawString(MC2Data.hmLocationRev.get(i),400,48+3*i*Constant.size);
								g.drawString(MC2Data.LocationNum[i]+" "+						//数据信息
										String.format("%.2f",(degree[i+1]-degree[i])/3.6)+"%",
										400,52+3*i*Constant.size+Constant.size);
							}
						};
					};
					panel.setBounds(100,100,400,300);
					//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.add(panel);
					frame.setVisible(true);
				}else if(combo.getSelectedIndex()==1){	//画一对消息分布图
					int from=Integer.parseInt(JOptionPane.showInputDialog("请输入ID(from)"));	//读取输入
					int to=Integer.parseInt(JOptionPane.showInputDialog("请输入ID(to)"));
					ArrayList<MC2Data> ret=null;
					try{
						ret=MC2Data.hmNow.get(MC2Data.hmIDfromNow.get(from)).get(MC2Data.hmIDtoNow.get(to));	//获得对应ID组的ArrayList
					}catch(Exception err){}
					if(ret!=null){
						JFrame frame = new JFrame("一对ID通信状况");	//标题
						frame.setBounds(100,100,600,300);
						final ArrayList<MC2Data> datas=ret;
						JPanel panel=new JPanel(){
							public void paintComponent(Graphics g) {
								String title;
								title="From "+from+" To "+to+" 通信量分布";	//图的标题
								g.setColor(Color.BLACK);
								g.setFont(new Font("隶书",Font.PLAIN,Constant.size));
								g.drawString(title, (this.getWidth()-title.length()*Constant.size/2)/2, Constant.size*2);
								g.drawLine(20, 20, 20, this.getHeight()-20);	//y轴
								g.drawLine(20, this.getHeight()-20, this.getWidth()-20, this.getHeight()-20);	//x轴
								
								for(Byte i=0;i<MC2Data.hmLocation.size();i++){	//绘制图例
									g.setColor(DrawPanel.locColor[i]);
									g.fillRect(450-Constant.size*3/2, 50+2*i*Constant.size-Constant.size, Constant.size, Constant.size);
									g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));
									g.setColor(Color.BLACK);
									g.drawString(MC2Data.hmLocationRev.get(i),450,48+2*i*Constant.size);
								}
								
								for(int i=0;i<10;i++){	//y轴划分
									g.drawLine(20, this.getHeight()-20*(i+1),
											22, this.getHeight()-20*(i+1));
									g.drawString(""+i,0,this.getHeight()-20*(i+1)+Constant.size/2);
								}
								g.drawString("times",0,Constant.size);
								int[] arr=new int[17];
								int width=(this.getWidth()-40)/18;
								for(int i=1;i<17;i++){	//x轴划分
									arr[i-1]=0;
									g.drawLine(20+i*width, this.getHeight()-18,
											20+i*width, this.getHeight()-20);
									g.drawString(""+(7+i), 16+i*width, this.getHeight()-6);
								}
								g.drawString("time(h)", 16+17*width, this.getHeight()-8);
								for(int i=0;i<datas.size();i++){	//画条形图
									MC2Data tmp=datas.get(i);
									int hour=tmp.date.getHours();
									arr[hour-8]++;	//一个地点一个方块
									g.setColor(DrawPanel.locColor[tmp.loc]);
									g.fillRect(20+(hour-7)*width-width/4,this.getHeight()-20-arr[hour-8]*20,
											width/2,19);
									//System.out.println(tmp.date+" "+tmp.date.getHours());
								}
							};
						};
						frame.add(panel);
						frame.setVisible(true);
						//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}
			}
		});
		
	}
}
