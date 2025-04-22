package in.newdevpoint.bootcamp.mapper;

import in.newdevpoint.bootcamp.dto.UserReq;
import in.newdevpoint.bootcamp.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    //    @Mapping(source = "id", target = "id")
    /****
 * Converts a UserEntity object to a UserReq data transfer object.
 *
 * @param sourceBean the UserEntity to convert
 * @return the corresponding UserReq object, or a default instance if sourceBean is null
 */
    UserReq mapToResponseEntity(UserEntity sourceBean);

    /**
 * Converts a UserReq DTO to a UserEntity object.
 *
 * @param sourceBean the UserReq DTO to convert
 * @return the corresponding UserEntity, or a default instance if sourceBean is null
 */
UserEntity mapFromReqToUserEntity(UserReq sourceBean);

    /**
     * Converts a list of UserEntity objects to a list of UserReq DTOs.
     *
     * @param sourceBeans the list of UserEntity objects to convert; may be null
     * @return a list of UserReq DTOs, or null if the input list is null
     */
    default List<UserReq> mapToResponseEntityList(List<UserEntity> sourceBeans) {
        if (sourceBeans == null) {
            return null;
        }

        ArrayList<UserReq> targetBeans = new ArrayList<>();
        for (UserEntity sourceBean : sourceBeans) {
            targetBeans.add(mapToResponseEntity(sourceBean));
        }
        return targetBeans;
    }
}
