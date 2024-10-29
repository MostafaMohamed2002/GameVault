package com.mostafadevo.freegames.domain.mappers

import com.mostafadevo.freegames.data.remote.gamepower.dto.GiveawayDtoItem
import com.mostafadevo.freegames.domain.model.Giveaway

fun GiveawayDtoItem.toGiveaway(): Giveaway {
    return Giveaway(
        description = description,
        end_date = end_date,
        gamerpower_url = gamerpower_url,
        id = id,
        image = image,
        instructions = instructions,
        open_giveaway = open_giveaway,
        open_giveaway_url = open_giveaway_url,
        platforms = platforms,
        published_date = published_date,
        status = status,
        thumbnail = thumbnail,
        title = title,
        type = type,
        users = users,
        worth = worth
    )
}
