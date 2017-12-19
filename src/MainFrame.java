import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame{
	DrawPanel panel;
	ControlPanel ctrPanel;
	JScrollPane scrollPanel;
	public MainFrame() {
		panel=new DrawPanel();	//������ʼ��
		ctrPanel=new ControlPanel();
		scrollPanel = new JScrollPane();
		setBounds(0,0,950,700);	//���ô�С
		panel.setSize(850,600);
		scrollPanel.setAutoscrolls(true);	//���ù������
		scrollPanel.setDoubleBuffered(true);
		scrollPanel.setViewportView(panel);

		add(BorderLayout.CENTER,scrollPanel);
		add(BorderLayout.EAST,ctrPanel);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Constant.setWnd(this);	//��ʼ��Constant�е���ز���
		setVisible(true);
	}
	public static void main(String[] args) {
		MainFrame wnd=new MainFrame();

	}

}
