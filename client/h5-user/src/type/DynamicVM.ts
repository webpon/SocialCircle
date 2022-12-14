import DynamicType from "@/type/Dynamic.type";
import ImageType from "@/type/Image.type";

export default interface DynamicVM {
  images?: Array<ImageType>;
  dynamic: DynamicType;
}
