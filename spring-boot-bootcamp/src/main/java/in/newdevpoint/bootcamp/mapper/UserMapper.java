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
    //    @Mapping(source = "username", target = "username")
    UserReq mapToResponseEntity(UserEntity sourceBean);

    UserEntity mapFromReqToUserEntity(UserReq sourceBean);

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
