The initial DatePicker was designed by outside sources. 
I have rebuild it, so that it can be put into NetBeans (or possibly other IDE) and then one can simply
drag and drop it onto the form (jPanel).

You will need to compile it, and then put the generated jar file on your computer where you indend to 
store your own java class library. You will also need the jdatepicker-1.3.2.jar and include this into your
Libray in your NetBeans IDE. This jdatepicker-1.3.2.jar can be downloaded from 
http://www.java2s.com/Code/Jar/j/Downloadjdatepicker132jar.htm. If you have an issue you can also contact me
on wernersa@bigpond.net.au and I will provide it for you. This component is free.

When you include this component onto your jPanel (drag and drop from the Pallett of your IDE) also ensure 
that in addition you also include the jdatepicker-1.3.2.jar in the project where this drag and drop DatePicker 
is being used.

The initial date picker as it is from the jdatepicker-1.3.2.jar can be used without my codes here, but my codes will 
a) make it a drag and drop component and 
b) also has additional features that make the use of this component
more versitile and and easier to use.

One of the features is, that you can set the minimum and maximum date permissible, and if the user clicks a date
that is outside this range, it will automatically reset to the min or max date. The user can also set up if this
will give an Error Message if a date has been selected outside the given range.

This modified date picker privides a large range of output option. Date can be out putted in Date or as a String 
value and the format of this String (for example yyyy-MM-dd, dd/MM/yyyy) can be set by the user. This is particularly
handy if using a SQLite database and you use the format yyyy-MM-dd so that you can run queries such as:
SELECT SUM(TAXAMOUNT) AS TOTALTAX FROM INCOME WHERE (DATE >= '2015-01-01') AND (DATE <= '2015-12-31').

Copy the codes into your NetBeans IDE, make sure you include the jdatepicker-1.3.2.jar to your libray, compile this
file as a NetBeans JavaClass Library (it has and needs no public void main entrance point) and build the jar file.
Copy the built jar file to the directory of your computer where you store your additional java libraries and you are 
ready to put it onto your NetBeans IDE Pallette. You can create you own space on the pallette and call is something
like My Components. You can then open an existing java form project and start one, and you must then add the generated
jar file plus the jdatepicker-1.3.2.jar to your library. If you don't add the jdatepicker-1.3.2.jar to your library as
well, the compiler will spit the dummy.

If you have issues you can contact me: wernersa@bigpond.net.au.
ENJOY! - Werner Sandner

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import Formatter.DateLabelFormatter;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class JDatePicker extends javax.swing.JPanel 
{
    /**
     * Creates new form JDatePicker
     */
    private String ErrorMessage;
    private final DateLabelFormatter formatter;
    private JDatePickerImpl datePicker;
    private ModuleType moduleType;
    private boolean showDateOnStartUpInTextBox;
    private boolean showOutOfRangeMessage;
    Date minDate;
    Date maxDate;
    
    public JDatePicker() 
    {
        initComponents();
        this.showOutOfRangeMessage = false;
        minDate = getDefaultMinDate();
        maxDate = getDefaultMaxDate();
        this.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        this.showDateOnStartUpInTextBox = true;
        moduleType = ModuleType.UtilDateModel;
        this.ErrorMessage = "";
        formatter = new DateLabelFormatter();
        selectAppropriateDatePickerModel();
    }   
   
    private void selectAppropriateDatePickerModel()
    {
        if(moduleType == ModuleType.UtilDateModel)
        {
            InitializeUtilDateModelDatePicker();
        }
        else if(moduleType == ModuleType.CalendarDateModel)
        {
            InitializeUtilCarlendarDateModelDatePicker();
        }
        else if(moduleType == ModuleType.SQLDateModel)
        {
            InitializeSQLDateModelDatePicker();
        }
        else//Default is the ModuleType.UtilDateModel
        {
            InitializeUtilDateModelDatePicker();
        }
    }
    private void InitializeUtilDateModelDatePicker()
    {
        try
        {
            UtilDateModel model = new UtilDateModel();
            //We set the default Text to todays date **************************
            int[] delement = getCurrentDayMonthYear();            
            model.setDate(delement[2], delement[1], delement[0]);//You can set a date as the intial date
            model.setSelected(this.showDateOnStartUpInTextBox);//This will make the date displayed in the text box
            //*****************************************************************
            JDatePanelImpl datePanel = new JDatePanelImpl(model);
            datePicker = new JDatePickerImpl(datePanel, formatter);//Default formatter = dd/MM/yyyy
            datePicker.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) 
            {
                String command = e.getActionCommand();
                if(command.equals("Date selected"))
                {   
                    if((getDate().compareTo(minDate)) < 0)
                    {
                        setDate(minDate);
                        if(showOutOfRangeMessage)
                        {
                            JOptionPane.showMessageDialog(null, "AS YOU SELECTED A DATE WHICH WAS " + 
                                    "EARLIER THAN PERMITTED,\r\nTHE EARLIEST DATE HAS BEEN SET.", "INFO", JOptionPane.INFORMATION_MESSAGE);
                        }                       
                    }                    
                    if((getDate().compareTo(maxDate)) > 0)
                    {
                        setDate(maxDate);
                        if(showOutOfRangeMessage)
                        {
                            JOptionPane.showMessageDialog(null, "AS YOU SELECTED A DATE WHICH WAS " + 
                                    "LATER THAN PERMITTED,\r\nTHE LATEST DATE HAS BEEN SET.", "INFO", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }                    
                    //JOptionPane.showMessageDialog(null, getDate(), "INFO", JOptionPane.INFORMATION_MESSAGE);
                }
                
		//label.setText(datePicker.getDate().toString());
            }
            });
            
            datePicker.setToolTipText("DatePicker");        
            datePicker.setVisible(true);
            this.add(datePicker);        
        }
        catch(Exception exc)
        {
            this.ErrorMessage += exc.getMessage();
        }
    }
    
    private void InitializeUtilCarlendarDateModelDatePicker()
    {
        try
        {
            UtilCalendarModel model = new UtilCalendarModel();
            //We set the default Text to todays date **************************
            int[] delement = getCurrentDayMonthYear();            
            model.setDate(delement[2], delement[1], delement[0]);//You can set a date as the intial date
            model.setSelected(this.showDateOnStartUpInTextBox);//This will make the date displayed in the text box
            //*****************************************************************
            JDatePanelImpl datePanel = new JDatePanelImpl(model);
            datePicker = new JDatePickerImpl(datePanel, formatter);//Default formatter = dd/MM/yyyy
            datePicker.setToolTipText("DatePicker");        
            datePicker.setVisible(true);
            this.add(datePicker);        
        }
        catch(Exception exc)
        {
            this.ErrorMessage += exc.getMessage();
        }
    }
    
    private void InitializeSQLDateModelDatePicker()
    {
        try
        {
            SqlDateModel model = new SqlDateModel();
            //We set the default Text to todays date **************************
            int[] delement = getCurrentDayMonthYear();            
            model.setDate(delement[2], delement[1], delement[0]);//You can set a date as the intial date
            model.setSelected(this.showDateOnStartUpInTextBox);//This will make the date displayed in the text box
            //*****************************************************************
            JDatePanelImpl datePanel = new JDatePanelImpl(model);
            datePicker = new JDatePickerImpl(datePanel, formatter);//Default formatter = dd/MM/yyyy
            datePicker.setToolTipText("DatePicker");        
            datePicker.setVisible(true);
            this.add(datePicker);        
        }
        catch(Exception exc)
        {
            this.ErrorMessage += exc.getMessage();
        }
    }
    
    //Int 0 = day, int 1 = month, int 2 = year
    private int[] getCurrentDayMonthYear()
    {
        int[] details = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        details[0] = calendar.get(Calendar.DAY_OF_MONTH);
        details[1] = calendar.get(Calendar.MONTH);
        details[2] = calendar.get(Calendar.YEAR); 
        return details;
    }
    
    private Date getDefaultMinDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2001, 0, 1, 0, 0, 0);//1.1.2001
        return cal.getTime(); 
    }
    
    //Int 0 = day, int 1 = month, int 2 = year
    private Date getDefaultMaxDate()
    {
       Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2040, 0, 1, 0, 0, 0);//1.1.2040
        return cal.getTime(); 
    }
    
    /*
    Sammple codes:
    jDatePicker1.setDatePickerToNonDefault("dd-MM-yyyy", JDatePicker.ModuleType.UtilDateModel, true); or
    jDatePicker1.setDatePickerToNonDefault("dd-MM-yyyy", JDatePicker.ModuleType.SQLDateModel, true); or
    jDatePicker1.setDatePickerToNonDefault("dd-MM-yyyy", JDatePicker.ModuleType.CalendarDateModel, true);
    ---If you make the last parameter false, then it will not show any text (date value) in the text box
    unless you select a date first. --
    */
    public void setDatePickerToNonDefault(String dateFormattString, ModuleType selectedModuleType, 
            boolean showDateOnStartUpInTextBox)
    {
         /*If an instants of a DatePicker is already on the panel, we need to remove it,
        otherwise we get multiple DatePickers showing to the user as we are creating 
        another instants of it.
        */        
        this.removeAll();//Removes current DatePicker from the panel, if exist
        this.formatter.setDatePattern(dateFormattString);       
        //Now we need to create a new instance of the DatePicker with the new format
        //The previous instance may not be set up with this new format
        moduleType = selectedModuleType;
        this.showDateOnStartUpInTextBox = showDateOnStartUpInTextBox;
        selectAppropriateDatePickerModel();
    }
    public String getErrorMessage()
    {
        return this.ErrorMessage;
    }    
    
    public Object getDateObject()
    {
        return datePicker.getModel().getValue();        
    } 
    
    public void setTextEditable(boolean editable)
    {
        datePicker.setTextEditable(editable);
    }
    
    public int getDay()
    {
        return datePicker.getModel().getDay();
    }
    
    public int getMonth()
    {
        return datePicker.getModel().getMonth();
    }
    
    public int getYear() 
    {
        return datePicker.getModel().getYear();
    }
    
    public boolean isSelected()
    {
        return datePicker.getModel().isSelected();
    }
    
    //int 0=day, int 1=month int 2=year
    public int[] getDayMonthYearArray()
    {
        int[] x = new int[3];
        x[0] = datePicker.getModel().getDay();
        x[1] = datePicker.getModel().getMonth();
        x[2] = datePicker.getModel().getYear();
        return x;
    }
    
    //Gets the Date in Date variable irrescpective which model type is used
    public Date getDate()
    {
        int day = datePicker.getModel().getDay();
        int month = datePicker.getModel().getMonth();
        int year = datePicker.getModel().getYear();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();             
    }
    
    //Get the date value already formatted for the SQLite Database
    public String getDateSQLString_yyyy_MM_dd()
    {
        Date date = getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    
    //Get the date value already formatted for the SQLite Database
    public String getDateSQLString_dd_fwdslash_mm_fwdslash_yyyy()
    {
        Date date = getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
    
    //Input parameter sample "dd.MM.yyyy", or dd-mm-yyyy or any other
    public String getDateCustomFormatted(String formatExpression)
    {
        Date date = getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatExpression);
        return dateFormat.format(date);
    }
    
    public void setDate(int year, int month, int day)
    {
        datePicker.getModel().setDate(year, month, day);
    }
    
    public void setDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        datePicker.getModel().setDate(year, month, day);
    }
    
    public void setDateFromSQLString_yyyy_MM_dd(String yyyy_MM_dd)
    {
        String delimiter = "-";
        String[] dateElements = yyyy_MM_dd.split(delimiter);
        int year = Integer.parseInt(dateElements[0]);//1953-
        int month = Integer.parseInt(dateElements[1]);//    12-
        int day = Integer.parseInt(dateElements[2]);//  
        datePicker.getModel().setDate(year, (month-1), day);//Month tested ok
    }
    
    public void setDay(int day)
    {
        datePicker.getModel().setDay(day);
    }
    
    public void setMonth(int month)
    {
        datePicker.getModel().setMonth(month);
    }
    
    public void setYear(int year)
    {
        datePicker.getModel().setYear(year);
    }
    
    public void addDays(int numberOfDaysToAdd)
    {
        datePicker.getModel().addDay(numberOfDaysToAdd);
    }
    
    public void addMonths(int numberOfMonthsToAdd)
    {
        datePicker.getModel().addMonth(numberOfMonthsToAdd);
    }
    
    public void addYears(int numberOfYearsToAdd)
    {
        datePicker.getModel().addYear(numberOfYearsToAdd);        
    }
    
    public void setBackgoundColor(Color color)
    {
        datePicker.setBackground(color);
    }
            
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    /*
    Sample code from the user end, to see how this function can be used and how this
    function can be usefull.
    if(jDatePicker1.getModuleType() == JDatePicker.ModuleType.UtilDateModel)
    {
        Date selectedDate = (Date) jDatePicker1.getDateObject();
    }
    else if(jDatePicker1.getModuleType() == JDatePicker.ModuleType.CalendarDateModel)
    {
        Calendar selectedValue = (Calendar) jDatePicker1.getDateObject();
        Date selectedDate = selectedValue.getTime();
    }
    else if(jDatePicker1.getModuleType() == JDatePicker.ModuleType.SQLDateModel)
    {
        java.sql.Date selectedDate = (java.sql.Date) jDatePicker1.getDateObject();
    }      
    */
    public ModuleType getModuleType()
    {
        return moduleType;
    }

    // Variables declaration - do not modify                     
    // End of variables declaration  
    public static enum ModuleType 
    {
        UtilDateModel, CalendarDateModel, SQLDateModel
    };
    
    /*Set the color of the panel this date picker is mounted on.
    Hint: set it to the same color as the date picker background.
    */
    public void setParentColor(Color color)
    {
        this.setBackgoundColor(color);
    }
    
    /*
    Set the entire back gound color of the date picker
    */
    public void setColor(Color color)
    {
        this.setBackgoundColor(color);
        datePicker.setBackground(color);
    }
    
    public void setDateLimit(Date minDate, Date maxDate)
    {
        this.minDate = minDate;
        this.maxDate = maxDate;        
    }
    
    public void resetDateLimitToDefault()
    {
        this.minDate = getDefaultMinDate();
        this.maxDate = getDefaultMaxDate();    
    }
    /*
    This does not seem to be doing anything.
    */    
    public void setToolTipTextOfDatePicker(String tooltiptext)
    {
        //this.setToolTipText(tooltiptext);//Don't do this, runs into an indefenite loop
        datePicker.setToolTipText(tooltiptext);
    }
    
    public void setShowOutOfRangeMessage()
    {
        this.showOutOfRangeMessage = true;
    }
    
    public void resetShowOutOfRangeMessage()
    {
        this.showOutOfRangeMessage = false;
    }
    
    public void setShowOutOfRangeMessage(boolean value)
    {
        this.showOutOfRangeMessage = value;
    }
    
    
    
    
}
