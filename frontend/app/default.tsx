// page được render khi not found
import Category from "@/components/home/category";
import FeaturedProduct from "@/components/home/featuredProduct";
// ưu tiên thứ 2 rôi tới page
export default function Default(){

    return (
        <div className="homepage">
            {/*<Banner></Banner>*/}
            <Category></Category>
            <FeaturedProduct></FeaturedProduct>
        </div>
    );
}