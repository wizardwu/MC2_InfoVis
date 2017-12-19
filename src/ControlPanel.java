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

public class ControlPanel extends JPanel{	//�������
	JButton btnMouseSelect;	//ѡȡģʽѡ��
	JTextField IDXL;	//xl
	JTextField IDXR;	//xr
	JTextField IDYL;	//yl
	JTextField IDYR;	//yr
	public ControlPanel(){
		setLayout(new GridBagLayout());	//���ò���
		GridBagConstraints gbc;        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(2, 1, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.LINE_START;
        
        ButtonGroup btnGroup=new ButtonGroup();	//ʹ��ѡ���Ϊһ��
        //JLabel dayLabel=new JLabel("ѡ��������ģʽ");
        JCheckBox sameID=new JCheckBox("����ID����");
        JCheckBox samesubID=new JCheckBox("��ͼ����");
		JRadioButton Friday=new JRadioButton("Friday");
		JRadioButton Saturday=new JRadioButton("Saturday");
		JRadioButton Sunday=new JRadioButton("Sunday");
        btnGroup.add(Friday);	//ʹ��ѡ���Ϊһ��
        btnGroup.add(Saturday);
        btnGroup.add(Sunday);
        //add(dayLabel);
        add(sameID);
        gbc.gridy = GridBagConstraints.RELATIVE;
        add(samesubID);
        add(Friday, gbc);
        add(Saturday, gbc);
        add(Sunday, gbc);
		JButton btnStart=new JButton("����");
		add(btnStart,gbc);
		JComboBox<String> combo=new JComboBox<String>(new String[]{"�ص�ֲ���ͼ","һ��ID��Ϣ"});
		JButton showChart=new JButton("��ʾ");
		add(combo);
		add(showChart);
		JLabel timeInfo=new JLabel("ʱ��:(hh:mm:ss)");
		JTextField timeL=new JTextField();
		JTextField timeR=new JTextField();
		JButton timebtn=new JButton("��ʱ��ɸѡ");
		add(timeInfo,gbc);
		add(timeL,gbc);
		add(timeR,gbc);
		add(timebtn,gbc);
		
		JLabel rangeInfo=new JLabel("hmID��Χ");
		btnMouseSelect=new JButton("���ѡȡ");
		IDXL=new JTextField("XL");
		IDXR=new JTextField("XR");
		IDYL=new JTextField("YL");
		IDYR=new JTextField("YR");
		JButton IDbtn=new JButton("��hmIDɸѡ");
		add(rangeInfo);
		add(btnMouseSelect);
		add(IDXL,gbc);
		add(IDXR,gbc);
		add(IDYL,gbc);
		add(IDYR,gbc);
		add(IDbtn,gbc);

		
		JButton IDXexchange=new JButton("����XID");
		JButton IDYexchange=new JButton("����YID");
		add(IDXexchange);
		add(IDYexchange);
		
		
		add(Constant.xLabel,gbc);
		add(Constant.yLabel,gbc);
		Constant.Info.setEditable(false);
		Constant.Info.setLineWrap(true);
		Constant.Info.setWrapStyleWord(true);
		Constant.Info.setText(Constant.EmptyInfo);
		add(Constant.Info,gbc);
		
		btnStart.addActionListener(new ActionListener() {	//��Ӧ���ذ�ť
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sameID.isSelected()){	//�Ƿ����ID����
					Constant.sameID=true;
				}else{
					Constant.sameID=false;
				}
				if(samesubID.isSelected()){	//��ͼ�Ƿ����ID����
					Constant.samesubID=true;
				}else{
					Constant.samesubID=false;
				}
				if(Friday.isSelected()){	//����ѡ��
					Constant.init(0);
				}
				if(Saturday.isSelected()){
					Constant.init(1);
				}
				if(Sunday.isSelected()){
					Constant.init(2);
				}
				MC2Data.setOverview();	//��ʾ���������ͼ
			}
		});
		btnMouseSelect.addActionListener(new ActionListener() {	//��Ӧ���ѡȡ��ť
			@Override
			public void actionPerformed(ActionEvent e) {
				btnMouseSelect.setEnabled(false);	//���ð�ť
				Constant.ModeSelect=true;			//����ѡȡģʽ
			}
		});
		timebtn.addActionListener(new ActionListener() {	//��Ӧʱ��ɸѡ
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
		
		IDbtn.addActionListener(new ActionListener() {	//��ӦIDɸѡ
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.filtByID(Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()),
						Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()));
			}
		});
		IDXexchange.addActionListener(new ActionListener() {	//X����
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.exchangeX(Integer.parseInt(IDXL.getText()),Integer.parseInt(IDXR.getText()));
			}
		});
		IDYexchange.addActionListener(new ActionListener() {	//Y����
			@Override
			public void actionPerformed(ActionEvent e) {
				MC2Data.exchangeY(Integer.parseInt(IDYL.getText()),Integer.parseInt(IDYR.getText()));
			}
		});
		
		showChart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(combo.getSelectedIndex()==0){	//����Ϣ�ֲ���ͼ
					JFrame frame = new JFrame("��Ϣ���͵ص�ֲ�");
					frame.setBounds(100,100,600,300);
					JPanel panel=new JPanel(){
						public void paintComponent(Graphics g) {
							String text="��Ϣ���͵ص�ֲ�";		//����
							g.setFont(new Font("����",Font.PLAIN,20));
							g.drawString(text, 200-text.length()*20/2, 40);
							double sum=0;
							double[] degree=new double[MC2Data.LocationNum.length+1];
							for(int i=0;i<MC2Data.LocationNum.length;i++){	//���
								sum+=MC2Data.LocationNum[i];
							}
							degree[0]=0;
							for(Byte i=0;i<MC2Data.LocationNum.length;i++){	//����������Ʊ�ͼ
								degree[i+1]=MC2Data.LocationNum[i]*360/sum+degree[i];	//�������ʼ�ߵĶ���
								g.setColor(DrawPanel.locColor[i]);	//������ɫ
								g.fillArc(100, 50, 200, 200, (int)degree[i], (int)(degree[i+1]-degree[i])+1);	//����ͼ
								g.fillRect(400-Constant.size*3/2, 50+3*i*Constant.size-Constant.size, Constant.size, Constant.size);//��ͼ��
								g.setColor(Color.BLACK);
								g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));	//����
								g.drawString(MC2Data.hmLocationRev.get(i),400,48+3*i*Constant.size);
								g.drawString(MC2Data.LocationNum[i]+" "+						//������Ϣ
										String.format("%.2f",(degree[i+1]-degree[i])/3.6)+"%",
										400,52+3*i*Constant.size+Constant.size);
							}
						};
					};
					panel.setBounds(100,100,400,300);
					//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.add(panel);
					frame.setVisible(true);
				}else if(combo.getSelectedIndex()==1){	//��һ����Ϣ�ֲ�ͼ
					int from=Integer.parseInt(JOptionPane.showInputDialog("������ID(from)"));	//��ȡ����
					int to=Integer.parseInt(JOptionPane.showInputDialog("������ID(to)"));
					ArrayList<MC2Data> ret=null;
					try{
						ret=MC2Data.hmNow.get(MC2Data.hmIDfromNow.get(from)).get(MC2Data.hmIDtoNow.get(to));	//��ö�ӦID���ArrayList
					}catch(Exception err){}
					if(ret!=null){
						JFrame frame = new JFrame("һ��IDͨ��״��");	//����
						frame.setBounds(100,100,600,300);
						final ArrayList<MC2Data> datas=ret;
						JPanel panel=new JPanel(){
							public void paintComponent(Graphics g) {
								String title;
								title="From "+from+" To "+to+" ͨ�����ֲ�";	//ͼ�ı���
								g.setColor(Color.BLACK);
								g.setFont(new Font("����",Font.PLAIN,Constant.size));
								g.drawString(title, (this.getWidth()-title.length()*Constant.size/2)/2, Constant.size*2);
								g.drawLine(20, 20, 20, this.getHeight()-20);	//y��
								g.drawLine(20, this.getHeight()-20, this.getWidth()-20, this.getHeight()-20);	//x��
								
								for(Byte i=0;i<MC2Data.hmLocation.size();i++){	//����ͼ��
									g.setColor(DrawPanel.locColor[i]);
									g.fillRect(450-Constant.size*3/2, 50+2*i*Constant.size-Constant.size, Constant.size, Constant.size);
									g.setFont(new Font("Courier New",Font.PLAIN,Constant.size));
									g.setColor(Color.BLACK);
									g.drawString(MC2Data.hmLocationRev.get(i),450,48+2*i*Constant.size);
								}
								
								for(int i=0;i<10;i++){	//y�Ữ��
									g.drawLine(20, this.getHeight()-20*(i+1),
											22, this.getHeight()-20*(i+1));
									g.drawString(""+i,0,this.getHeight()-20*(i+1)+Constant.size/2);
								}
								g.drawString("times",0,Constant.size);
								int[] arr=new int[17];
								int width=(this.getWidth()-40)/18;
								for(int i=1;i<17;i++){	//x�Ữ��
									arr[i-1]=0;
									g.drawLine(20+i*width, this.getHeight()-18,
											20+i*width, this.getHeight()-20);
									g.drawString(""+(7+i), 16+i*width, this.getHeight()-6);
								}
								g.drawString("time(h)", 16+17*width, this.getHeight()-8);
								for(int i=0;i<datas.size();i++){	//������ͼ
									MC2Data tmp=datas.get(i);
									int hour=tmp.date.getHours();
									arr[hour-8]++;	//һ���ص�һ������
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
