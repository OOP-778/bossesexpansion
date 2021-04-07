package org.royalix.bossesexpansion.plugin.config.filter

enum class FilterMethod(val index: Int) {
    WHITELIST(1),
    BLACKLIST(2),
    OFF(3);

    val next: FilterMethod
        get() = match(index + 1) ?: match(1)!!
    val previous: FilterMethod
        get() = match(index - 1) ?: match(3)!!

    companion object {
        fun match(name: String): FilterMethod {
            if (name.contentEquals("false"))
                return OFF

            for (value in values()) {
                if (value.name.equals(name, ignoreCase = true)) return value
            }
            throw IllegalStateException("Failed to find Filter Method by: $name")
        }

        fun match(index: Int): FilterMethod? {
            for (value in values()) {
                if (value.index == index) return value
            }
            return null
        }
    }
}
