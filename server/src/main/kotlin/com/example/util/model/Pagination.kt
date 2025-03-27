package com.example.util.model

data class Pagination(
    val totalPage: Int,
    val currentPage: Int,
    val totalElements: Int,
) {
    private val hasNextPage = currentPage < totalPage

    private fun nextPage() = this.copy(currentPage = currentPage + 1)

    private val isLastPage = currentPage == totalPage

    val currentPageElements = if (isLastPage) totalElements % 10 else 10

    fun <T> map(
        block: (Pagination) -> T,
    ): MutableList<T> {
        val list = mutableListOf<T>()

        repeat(this.totalPage) {
            if (isLastPage) return@repeat
            val result = block(this)
            list.add(result)
            if (hasNextPage) nextPage()
        }

        return list
    }

    companion object {
        fun of(
            totalElements: Int,
        ) =
            Pagination(
                totalPage = 0,
                currentPage = 0,
                totalElements = totalElements,
            )
    }
}