const bannerForm=document.getElementById('bannerForm');
const bannerImage=document.getElementById('bannerImage');
const bannerPosition=document.getElementById('bannerPosition');
const bannerStatus=document.getElementById('bannerStatus');
const bannerDescription=document.getElementById('bannerDescription');
const bannerHeading=document.getElementById('bannerHeading');

const inputImageDiv=document.getElementById('inputImageDiv');
const imageCropButton=document.getElementById('imgCropBtn');
const imageOutput=document.getElementById('imageOutput');


let formData=null;

window.onload=(e)=>{
    imageCropButton.hidden=true;
}

bannerImage.addEventListener('change',(e)=>{

    let fileReader=new FileReader();

    fileReader.onload=(e)=>{

        if(e.target.result){
            let img=document.createElement('img');
            img.id='currentImage'
            img.src=e.target.result;
            inputImageDiv.innerHTML='';
            inputImageDiv.appendChild(img);



            const cropper=new Cropper(img,{
                aspectRatio:16/9,
                viewMode:0,
            });

            imageCropButton.hidden=false;

            //name generator
            let name=Math.random().toString(36).substring(2,7);

            imageCropButton.onclick=(e)=>{
                imageOutput.src=cropper.getCroppedCanvas().toDataURL();
                cropper.getCroppedCanvas().toBlob((blob)=>{
                     formData=new FormData();
                        formData.append("imageFile",blob,name);
                })
            }

        }
    };
    fileReader.readAsDataURL(e.target.files[0]);
})



bannerForm.addEventListener('submit',(e)=>{
    e.preventDefault()

    formData.append('bannerHeading'   ,bannerHeading.value)
    formData.append('bannerStatus'  ,   bannerStatus.value)
    formData.append('bannerPosition'  ,bannerPosition.value)
    formData.append('bannerDescription', bannerDescription.value )


    let endpoint='/admin/banners/add';


    fetch(endpoint,{
        method:'post',
        body:formData

    }).catch(console.error)

    alert("banner created successfully");
    window.location='/admin/banners';

})






