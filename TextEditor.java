import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class TextEditor extends JFrame implements ActionListener,DocumentListener
{
	JMenuBar mb;
	JMenu m1,m2;
	JMenuItem i1,i2,i3,i4,i5,i6,i7,i8;

	JTextArea ta;
	JTextField tf;

	JToolBar tb;
	JButton b1,b2,b3;

	JPopupMenu pm;
	JMenuItem pmi1,pmi2,pmi3;

	JFileChooser fc;
	File f = null;
	boolean isChanged = false;

	TextEditor()
	{
		setTitle("TextEditor : Untitled");

		i1 = new JMenuItem("New");
		i2 = new JMenuItem("Open");
		i3 = new JMenuItem("Save");
		i4 = new JMenuItem("Save As");
		i5 = new JMenuItem("Exit");
		i5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));

		i6 = new JMenuItem("Cut");
		i6.setActionCommand("C");
		i7 = new JMenuItem("Copy");
		i8 = new JMenuItem("Paste");

		m1 = new JMenu("File");
		m2 = new JMenu("Edit");

		fc = new JFileChooser(".");
		FileNameExtensionFilter fl1 = new FileNameExtensionFilter("Java Files","java");
		fc.addChoosableFileFilter(fl1);
		fc.setFileFilter(fl1);

		m1.add(i1);m1.add(i2);m1.add(i3);m1.add(i4);
		m1.addSeparator();
		m1.add(i5);

		m2.add(i6);m2.add(i7);m2.add(i8);

		mb = new JMenuBar();
		mb.add(m1);mb.add(m2);

		setJMenuBar(mb);

		ta = new JTextArea();
		ta.setFont(new Font("Serif",Font.PLAIN,25));
		add(new JScrollPane(ta));

		tf = new JTextField();
		add(tf,"South");
		tf.setFont(new Font("Serif",Font.PLAIN,25));
		tf.setText("Total Chars : 0");
		//add(fc);

		b1 = new JButton("");b1.setIcon(new ImageIcon("cut.gif"));
		b1.setActionCommand("C");
		b2 = new JButton("");b2.setIcon(new ImageIcon("copy.gif"));
		b3 = new JButton("");b3.setIcon(new ImageIcon("paste.gif"));
		tb = new JToolBar();
		tb.add(b1);tb.add(b2);tb.add(b3);

		add(tb,"North");

		pmi1 = new JMenuItem("Cut");
		pmi1.setActionCommand("C");
		pmi2 = new JMenuItem("Copy");
		pmi3 = new JMenuItem("Paste");
		pm = new JPopupMenu();
		pm.add(pmi1);pm.add(pmi2);pm.add(pmi3);

		ta.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent evt)
			{
				if(evt.isPopupTrigger())
				{
					pm.show(ta,evt.getX(),evt.getY());
				}
			}
		});

		i1.addActionListener(this);
		i2.addActionListener(this);
		i3.addActionListener(this);
		i4.addActionListener(this);
		i5.addActionListener(this);
		i6.addActionListener(this);
		i7.addActionListener(this);
		i8.addActionListener(this);

		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);

		pmi1.addActionListener(this);
		pmi2.addActionListener(this);
		pmi3.addActionListener(this);

		ta.getDocument().addDocumentListener(this);

		setSize(600,400);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				saveContent();
			}
		});
	}
	public void changedUpdate(DocumentEvent evt)
	{
	}
	public void removeUpdate(DocumentEvent evt)
	{
		isChanged = true;
		tf.setText("Total Characters : "+ta.getText().length());
	}
	public void insertUpdate(DocumentEvent evt)
	{
		isChanged = true;
		tf.setText("Total Characters : "+ta.getText().length());
	}
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getSource()==i1)
		{
			saveContent();
			ta.setText("");
			setTitle("TextEditor : Untitled");
			isChanged=false;
		}
		if(evt.getSource()==i2)
		{
			saveContent();
			int x = fc.showOpenDialog(this);
			if(x == JFileChooser.APPROVE_OPTION)
			{
				f = fc.getSelectedFile();
				setTitle("TextEditor : "+f.getAbsolutePath());
				try
				{
					FileReader reader = new FileReader(f);
					int z = (int)f.length();
					char[] arr = new char[z];
					reader.read(arr);
					reader.close();
					String str = new String(arr);
					ta.setText(str);
				}
				catch(Exception ex){}
			}
			isChanged=false;
		}
		if(evt.getSource()==i3)
		{
			if(f==null)
				saveFileWithName();
			else
				saveFileWithoutName();
		}
		if(evt.getSource()==i4)
		{
			saveFileWithName();
		}
		if(evt.getSource()==i5)
		{
			dispose();
		}
		if(evt.getActionCommand().equals("C"))
		{
			ta.cut();
		}
		if(evt.getSource()==i7 || evt.getSource()==b2)
		{
			ta.copy();
		}
		if(evt.getSource()==i8 || evt.getSource()==b3)
		{
			ta.paste();
		}
	}

	void saveFileWithoutName()
	{
		try
		{
			FileWriter writer = new FileWriter(f);
			String str = ta.getText();
			char[] arr = str.toCharArray();
			writer.write(arr);
			writer.close();
		}
		catch(Exception ex){}
		isChanged=false;
	}
	void saveFileWithName()
	{
		int x = fc.showSaveDialog(this);
		if(x == JFileChooser.APPROVE_OPTION)
		{
			f = fc.getSelectedFile();
			if(f.exists())
			{
				int a = JOptionPane.showConfirmDialog(this,"Do you wish to Overwrite","Confirm",JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.NO_OPTION)
					return;
			}
			try
			{
				FileWriter writer = new FileWriter(f);
				String str = ta.getText();
				char[] arr = str.toCharArray();
				writer.write(arr);
				writer.close();
			}
			catch(Exception ex){}
		}
		setTitle("TextEditor : "+f.getAbsolutePath());
		isChanged=false;
	}
	public void saveContent()
	{
		if(isChanged==true)
		{
			int x = JOptionPane.showConfirmDialog(this,"Do you want to save Changes","Confirm",JOptionPane.YES_NO_OPTION);
			if(x==JOptionPane.YES_OPTION)
			{
				if(f==null)
					saveFileWithName();
				else
					saveFileWithoutName();
			}
		}
	}

	public static void main(String args[])
	{
		TextEditor f = new TextEditor();
		f.setVisible(true);
	}
}
