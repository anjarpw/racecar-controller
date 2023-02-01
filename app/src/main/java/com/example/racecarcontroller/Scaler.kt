import android.util.Range

class Scaler(
    virtualRange: Range<Float>,
    getViewRange: () -> Range<Int>,
    isInverted: Boolean = false
) {

    var virtualRange = virtualRange
    val getViewRange = getViewRange
    var isInverted = isInverted

    private fun getVirtualFrom(): Float {
        return if (isInverted) virtualRange.upper else virtualRange.lower
    }

    private fun getVirtualTo(): Float {
        return if (isInverted) virtualRange.lower else virtualRange.upper
    }

    fun updateVirtualRange(virtualRange: Range<Float>, isInverted: Boolean) {
        this.virtualRange = virtualRange
        this.isInverted = isInverted
    }

    private fun getScaleViewToVirtual(viewRange: Range<Int>): Float {
        return (viewRange.upper - viewRange.lower) / (getVirtualTo() - getVirtualFrom())
    }

    private fun getScaleVirtualToView(viewRange: Range<Int>): Float {
        return (getVirtualTo() - getVirtualFrom()) / (viewRange.upper - viewRange.lower)
    }

    fun toView(virtualValue: Float): Float {
        val viewRange = getViewRange()
        return (virtualValue - getVirtualFrom()) * getScaleViewToVirtual(viewRange) + viewRange.lower
    }

    fun toVirtual(viewValue: Float): Float {
        val viewRange = getViewRange()
        return (viewValue - viewRange.lower) * getScaleVirtualToView(viewRange) + getVirtualFrom()
    }

    companion object{
        fun clip(range: Range<Float>, value: Float): Float{
            var value = value
            if(value < range.lower){
                value = range.lower
            }
            if(value > range.upper){
                value = range.upper
            }
            return value
        }
        fun clip(range: Range<Int>, value: Int): Int{
            var value = value
            if(value < range.lower){
                value = range.lower
            }
            if(value > range.upper){
                value = range.upper
            }
            return value
        }
    }
}
