package com.thaipd.android.app.truyenkimdung.data;

import android.content.Context;

import com.thaipd.android.app.truyenkimdung.R;

public enum Stories {
	
/*	ANH_HUNG_XA_DIEU(1),
	BACH_MA_KHIEU_TAY_PHONG(2),
	BICH_HUYET_KIEM(3),
	HIEP_KHACH_HANH(4),
	LIEN_THANH_QUYET(5),
	LOC_DINH_KY(6),
	PHI_HO_NGOAI_TRUYEN(7),
	THAN_DIEU_HIEP_LU(8),
	THIEN_LONG_BAT_BO(9),
	THU_KIEM_AN_CUU_LUC(10),
	TUYET_SON_PHI_HO(11),
	UYEN_UONG_DAO(12),
	VIET_NU_KIEM(13),
	Y_THIEN_DO_LONG_KY(14),
	TIEU_NGAO_GIANG_HO(15);*/
	
	THIEN_LONG_BAT_BO(1),
	LOC_DINH_KY(2),
	ANH_HUNG_XA_DIEU(3),
	THAN_DIEU_HIEP_LU(4),
	Y_THIEN_DO_LONG_KY(5),
	HIEP_KHACH_HANH(6),
	TIEU_NGAO_GIANG_HO(7),
	BACH_MA_KHIEU_TAY_PHONG(8),
	BICH_HUYET_KIEM(9),
	LIEN_THANH_QUYET(10),
	PHI_HO_NGOAI_TRUYEN(11),
	TUYET_SON_PHI_HO(12),
	THU_KIEM_AN_CUU_LUC(13),
	UYEN_UONG_DAO(14),
	VIET_NU_KIEM(15);
	
	private Stories(int code) {
		this.code = code;
	}
	
	private int code;
	
	public static Stories getByCode(int code) {
		
		for (Stories story : Stories.values()) {
			if (story.getCode() == code) {
				return story;
			}
		}
		return Stories.ANH_HUNG_XA_DIEU;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getString(Context mContext) {
		String str = "";
		Stories.ANH_HUNG_XA_DIEU.getCode();
		switch (code) {
		case 0:
			str = mContext.getString(R.string.story_name_anh_hung_xa_dieu);
			break;
			case 1:
				str = mContext.getString(R.string.story_name_bach_ma);
				break;
			case 2:
				str = mContext.getString(R.string.story_name_bich_huyet_kiem);
				break;
			case 3:
				str = mContext.getString(R.string.story_name_hiep_khach_hanh);
				break;
			case 4:
				str = mContext.getString(R.string.story_name_lien_thanh_quyet);
				break;
			case 5:
				str = mContext.getString(R.string.story_name_loc_dinh_ky);
				break;
			case 6:
				str = mContext.getString(R.string.story_name_phi_ho_ngoai_truyen);
				break;
			case 7:
				str = mContext.getString(R.string.story_name_than_dieu_dai_hiep);
				break;
			case 8:
				str = mContext.getString(R.string.story_name_thien_long_bat_bo);
				break;
			case 9:
				str = mContext.getString(R.string.story_name_thu_kiem);
				break;
			case 10:
				str = mContext.getString(R.string.story_name_tuyet_son_phi_ho);
				break;
			case 11:
				str = mContext.getString(R.string.story_name_uyen_uong_dao);
				break;
			case 12:
				str = mContext.getString(R.string.story_name_viet_nu_kiem);
				break;
			case 13:
				str = mContext.getString(R.string.story_name_xa_dieu_tam_bo_khuc);
				break;
			case 14:
				str = mContext.getString(R.string.story_name_y_thien_do_long_ky);
				break;
		}
		
		return str;
	}
}
