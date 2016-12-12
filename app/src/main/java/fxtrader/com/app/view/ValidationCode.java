package fxtrader.com.app.view;

/**
 * Created by zhangyuzhu on 2016/12/7.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import fxtrader.com.app.R;

/**
 * Created by lt on 2016/3/2.
 */
public class ValidationCode extends View {
    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;
    /**
     * 验证码文本画笔
     */
    private Paint mTextPaint; // 文本画笔
    /**
     * 干扰点坐标的集合
     */
    private ArrayList<PointF> mPoints = new ArrayList<PointF>();
    private Random mRandom = new Random();
    ;
    /**
     * 干扰点画笔
     */
    private Paint mPointPaint;
    /**
     * 绘制贝塞尔曲线的路径集合
     */
    private ArrayList<Path> mPaths = new ArrayList<Path>();
    /**
     * 干扰线画笔
     */
    private Paint mPathPaint;
    /**
     * 验证码字符串
     */
    private String mCodeString;
    /**
     * 验证码的位数
     */
    private int mCodeCount;
    /**
     * 验证码字符的大小
     */
    private float mTextSize;
    /**
     * 验证码字符串的显示宽度
     */
    private float mTextWidth;

    /**
     * 在java代码中创建view的时候调用，即new
     *
     * @param context
     */
    public ValidationCode(Context context) {
        this(context, null);
    }

    /**
     * 在xml布局文件中使用view但没有指定style的时候调用
     *
     * @param context
     * @param attrs
     */
    public ValidationCode(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 在xml布局文件中使用view并指定style的时候调用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ValidationCode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrValues(context, attrs);
// 做一些初始化工作
        init();
    }

    /**
     * 获取布局文件中的值
     *
     * @param context
     */
    private void getAttrValues(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndentifyingCode);
        mCodeCount = typedArray.getInteger(R.styleable.IndentifyingCode_codeCount, 4);
        mTextSize = typedArray.getDimension(R.styleable.IndentifyingCode_textSize, typedArray.getDimensionPixelSize(R.styleable.IndentifyingCode_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics())));
        typedArray.recycle();
    }

    /**
     * 要像layout_width和layout_height属性支持wrap_content就必须重新这个方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initData();
        int length = mCodeString.length();
        float charLength = mTextWidth / length;
        for (int i = 1; i <= length; i++) {
            int offsetDegree = mRandom.nextInt(15);
            // 产生0和1，如果是1那么正旋转正角度，否则旋转负角度
            offsetDegree = mRandom.nextInt(2) == 1 ? offsetDegree : -offsetDegree;
            canvas.save();
            canvas.rotate(offsetDegree, mWidth / 2, mHeight / 2);
            mTextPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20);
            canvas.drawText(String.valueOf(mCodeString.charAt(i - 1)), (i - 1) * charLength * 1.6f + 50, mHeight * 2 / 3f, mTextPaint);
            canvas.restore();
        }
        // 产生干扰效果1 -- 干扰点
        for (PointF pointF : mPoints) {
            mPointPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20);
            canvas.drawPoint(pointF.x, pointF.y, mPointPaint);
        }
        // 产生干扰效果2 -- 干扰线
        for (Path path : mPaths) {
            mPathPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20);
            canvas.drawPath(path, mPathPaint);
        }
    }

    private void initData() {
        mHeight = getHeight();
        mWidth = getWidth();
        mPoints.clear();
        for (int i = 0; i < 150; i++) {
            PointF pointF = new PointF(mRandom.nextInt(mWidth) + 10, mRandom.nextInt(mHeight) + 10);
            mPoints.add(pointF);
        }
        mPaths.clear();
        for (int i = 0; i < 2; i++) {
            Path path = new Path();
            int startX = mRandom.nextInt(mWidth / 3) + 10;
            int startY = mRandom.nextInt(mHeight / 3) + 10;
            int endX = mRandom.nextInt(mWidth / 2) + mWidth / 2 - 10;
            int endY = mRandom.nextInt(mHeight / 2) + mHeight / 2 - 10;
            path.moveTo(startX, startY);
            path.quadTo(Math.abs(endX - startX) / 2, Math.abs(endY - startY) / 2, endX, endY);
            mPaths.add(path);
        }
    }

    /**
     * 初始化一些数据
     */
    private void init() {
// 生成随机数字和字母组合
        mCodeString = getCharAndNumr(mCodeCount);
// 初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(3); // 画笔大小为3
        mTextPaint.setTextSize(mTextSize); // 设置文字大小
// 初始化干扰点画笔
        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(6);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形
// 初始化干扰线画笔
        mPathPaint = new Paint();
        mPathPaint.setStrokeWidth(5);
        mPathPaint.setColor(Color.GRAY);
        mPathPaint.setStyle(Paint.Style.STROKE); // 设置画笔为空心
        mPathPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形
// 取得验证码字符串显示的宽度值
        mTextWidth = mTextPaint.measureText(mCodeString);
    }

    /**
     * java生成随机数字和字母组合
     *
     * @param
     * @return
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
// 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
// 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
// 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCodeString = getCharAndNumr(mCodeCount);
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     */
    private int measureWidth(int widthMeasureSpec) {
        int result = (int) (mTextWidth * 1.8f);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
// 精确测量模式，即布局文件中layout_width或layout_height一般为精确的值或match_parent
            result = widthSize; // 既然是精确模式，那么直接返回测量的宽度即可
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
// 最大值模式，即布局文件中layout_width或layout_height一般为wrap_content
                result = Math.min(result, widthSize);
            }
        }
        return result;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = (int) (mTextWidth / 1.6f);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
// 精确测量模式，即布局文件中layout_width或layout_height一般为精确的值或match_parent
            result = heightSize; // 既然是精确模式，那么直接返回测量的宽度即可
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
// 最大值模式，即布局文件中layout_width或layout_height一般为wrap_content
                result = Math.min(result, heightSize);
            }
        }
        return result;
    }

    public Boolean isEqualsIgnoreCase(String CodeString) {
        return mCodeString.equalsIgnoreCase(CodeString);
    }


    /**
     * 刷新验证码图片
     */
    public void refresh(){
        mCodeString = getCharAndNumr(mCodeCount);
        invalidate();
    }
}
