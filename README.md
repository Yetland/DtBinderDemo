# DtBinderDemo
## 概述
在使用MultiTypeAdapter实现RecyclerView多类型显示的时候，会创建一个ViewHolder和ViewBinder，此时如果要在Activity或者Fragment相应点击事件的时候，需要在ViewHolder和ViewBinder之间做传递。如果一个ViewHolder下有RecyclerView，然后也使用了MultiTypeAdapter，那么这个点击事件的回调将会是一件相当头疼的事情。
## 启发
在使用```LifeCycle```时，发现他只需要当前类实现```LifecycleObserver```，然后通过调用```addObserver```方法即可实现事件传递。由此想到点击事件是否也可以使用此种形式来实现。
## 效果图
![demo](https://github.com/Yetland/DtBinderDemo/blob/master/demo.gif)
## 实现
在受到```LifeCycle```的启发下，模仿这写了几个类。

- OnItemClick
```
abstract class OnItemClick {
    abstract fun addObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun addObserver(list: List<OnItemClickObserver>?)
    abstract fun getObserver(): List<OnItemClickObserver>?
    abstract fun dispatchObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun dispatchObserver(list: List<OnItemClickObserver>?)
    abstract fun removeObserver(onItemClickObserver: OnItemClickObserver)
    abstract fun removeAll()
}
定义一系列的操作方法，包括添加、移除、传递等。
```
- OnItemClickOwner
```
interface OnItemClickOwner {
    fun getOnItemClick(): OnItemClick
}
```
一个点击事件如果在某个类中如果需要做操作，那么需要实现该接口。
- OnItemClickRegistry
```
class OnItemClickRegistry() : OnItemClick() {
    // 省略具体代码
}
```
继承OnItemClick，并实现具体的方法。
- OnItemClickObserver
```
interface OnItemClickObserver
```
基本点击事件Observer

## ViewBinder/ViewHolder 改造
- BaseViewHolder
```
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickOwner {
    var mT: T? = null
    var mContext: Context = itemView.context
    abstract fun setData(t: T)
}
```
- BaseViewHolderWrapper
```
open class BaseViewHolderWrapper<T>(itemView: View) :
        BaseViewHolder<T>(itemView), View.OnClickListener {

    private val onItemRegistry = OnItemClickRegistry(this)

    override fun setData(@NonNull t: T) {
        mT = t
        itemView?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
    }

    fun <R : OnItemClickObserver> getObserver(clazz: Class<R>): R? {
        getOnItemClick().getObserver()?.forEach {
            if (clazz.isInstance(it)) {
                @Suppress("UNCHECKED_CAST")
                return it as R
            }
        }
        return null
    }


    override fun getOnItemClick(): OnItemClick {
        return onItemRegistry
    }
}
```
- BaseViewBinder
```
abstract class BaseViewBinder<T, R : BaseViewHolderWrapper<T>> : ItemViewBinder<T, R>(), OnItemClickOwner {

    private val onItemRegistry = OnItemClickRegistry(this)

    override fun getOnItemClick(): OnItemClick {
        return onItemRegistry
    }

    @LayoutRes
    private val layoutId: Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    init {
        layoutId = this.getLayoutId()
    }

    fun getItemView(inflater: LayoutInflater, parent: ViewGroup): View {
        if (layoutId == 0) {
            throw IllegalArgumentException("You must return a layout id.")
        }
        return inflater.inflate(layoutId, parent, false)
    }

    override fun onBindViewHolder(holder: R, item: T) {
        holder.getOnItemClick().dispatchObserver(getOnItemClick().getObserver())
        holder.setData(item)
    }
}
```
这一层，对事件在OnBindViewHolder中做了一次传递，通过dispatchObserver方法，将ViewBinder中的事件传递到了ViewHolder中。

## 使用
### Observer
```
interface Observer1 : OnItemClickObserver {
    fun onClick1()
}

interface Observer2 : OnItemClickObserver {
    fun onClick2()
}
```
定义两个点击事件。
### Bean
```
data class Bean1(var name: String)

class Bean2
```
创建实体类，和Binder相对应。
### ViewHolder
- ViewHolder1
```
class ViewHolder1(itemView: View) : BaseViewHolderWrapper<Bean1>(itemView) {
    private val textView = itemView.findViewById<TextView>(R.id.textView)
    override fun setData(t: Bean1) {
        super.setData(t)
        textView.text = t.name
    }

    override fun onClick(v: View) {
        getObserver(Observer1::class.java)?.onClick1()
    }
}
```
在覆写```setData```方法的时候，一定要调用super。不然事件无法传递。

响应事件的回调，只需要调用```getObserver```方法，然后传入对应的Observer，如果有，就直接调用方法。
- ViewHolder2
```
class ViewHolder2(itemView: View) : BaseViewHolderWrapper<Bean2>(itemView) {
    var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView2)
    private val adapter = RecyclerViewAdapter2(mContext)

    init {
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.submitList(listOf(
            Bean1("第二层"),
            Bean1("第二层"),
            Bean1("第二层")
        ))
    }

    override fun setData(t: Bean2) {
        super.setData(t)
        adapter.getOnItemClick().dispatchObserver(getOnItemClick().getObserver())
    }

    override fun onClick(v: View) {
        getObserver(Observer2::class.java)?.onClick2()
    }
}
```
adapter传递事件，也是通过dispatchObserver方法。
### ViewBinder
ViewBinder其实就不需要做什么事情了，比较简单。
```
class Binder1 : BaseViewBinder<Bean1, ViewHolder1>() {
    override fun getLayoutId(): Int {
        return R.layout.item_1
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder1 {
        return ViewHolder1(getItemView(inflater, parent))
    }
}

class Binder2 : BaseViewBinder<Bean2, ViewHolder2>() {
    override fun getLayoutId(): Int {
        return R.layout.item_2
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder2 {
        return ViewHolder2(getItemView(inflater, parent))
    }
}
```
### Activity
- 注册
```
val binder1 = Binder1()
val binder2 = Binder2()
multiTypeAdapter.register(Bean1::class.java, binder1)
multiTypeAdapter.register(Bean2::class.java, binder2)
```
- 添加点击事件
```
        binder1.getOnItemClick().addObserver(object : Observer1 {
            override fun onClick1() {
                Toast.makeText(this@MainActivity, "binder1 onClick1", Toast.LENGTH_SHORT).show()
            }
        })
        binder2.getOnItemClick().addObserver(object : Observer1 {
            override fun onClick1() {
                Toast.makeText(this@MainActivity, "binder2 onClick1", Toast.LENGTH_SHORT).show()
            }
        })
        binder2.getOnItemClick().addObserver(object : Observer2 {
            override fun onClick2() {
                Toast.makeText(this@MainActivity, "binder2 onClick2", Toast.LENGTH_SHORT).show()
            }
        })
```