# CalendarView
#### 日历列表控件
##### 由于Rv嵌套Rv的方式 性能太差 只好绘制出日历了

 ```
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
    dependencies {
	        implementation 'com.github.Edison0716:CalendarView:v1.0'
	}
 ```

 ```
   <com.junlong0716.pickerview.MonthView
        android:id="@+id/mv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ffffff"
        app:calendarDayCheckedLightColor="@color/calendarDayCheckedLightColor"<!--日期选中的背景颜色值-->
        app:calendarDayCheckedLightRadius="3dp"<!--选中的背景圆角-->
        app:calendarDayCheckedTextColor="@color/calendarDayCheckedTextColor"<!--日期选中的文字字体颜色-->
        app:calendarDayCommonTextColor="@color/calendarDayCommonTextColor"<!--日期正常显示的颜色值-->
        app:calendarDayDisableTextColor="@color/calendarDayDisableTextColor"<!--日期不可用的颜色值-->
        app:calendarDesCheckedTextColor="@color/calendarDayCheckedTextColor"<!--描述选中的文字字体颜色-->
        app:calendarDesCommonTextColor="@color/calendarDesCommonTextColor"<!--日期描述正常显示颜色值-->
        app:calendarDayTextSize="16dp"<!--日期文字大小-->
        app:calendarDesTextSize="14dp"<!--描述文字大小-->
        />
 ```
##### 食用方法

 ```
    //日期Day需要继承DayBaseEntity
    public class DayBaseEntity {
        private int day;
        private String des;
        private boolean isDisable;
    }
    
    //例如
    public class CalenderBean {
    private int month;
    private int year;
    private int setCheckedDay;
    private List<Day> days;
    private String groupName;
    //省略get set
    public static class Day extends DayBaseEntity{}
    }

    holder.mv.setCalendarParams(mList.get(position).getYear(), mList.get(position).getMonth(),mList.get(position).getDays());
 ```   
### 展示

<img src="https://github.com/Edison0716/CalendarView/blob/master/screen_shot/calendar_view1.jpg" width="360"/>

