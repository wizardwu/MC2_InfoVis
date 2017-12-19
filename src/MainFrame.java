import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame{
	DrawPanel panel;
	ControlPanel ctrPanel;
	JScrollPane scrollPanel;
	public MainFrame() {
		panel=new DrawPanel();	//变量初始化
		ctrPanel=new ControlPanel();
		scrollPanel = new JScrollPane();
		setBounds(0,0,950,700);	//设置大小
		panel.setSize(850,600);
		scrollPanel.setAutoscrolls(true);	//设置滚动面板
		scrollPanel.setDoubleBuffered(true);
		scrollPanel.setViewportView(panel);

		add(BorderLayout.CENTER,scrollPanel);
		add(BorderLayout.EAST,ctrPanel);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Constant.setWnd(this);	//初始化Constant中的相关部分
		setVisible(true);
	}
	public static void main(String[] args) {
		MainFrame wnd=new MainFrame();

	}

}
